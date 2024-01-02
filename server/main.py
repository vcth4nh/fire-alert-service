from firebase_admin.exceptions import FirebaseError
from flask import Flask, render_template, request, redirect, url_for, flash, session
from flask_session import Session
from firebase_admin import credentials, auth, initialize_app

app = Flask(__name__)
app.secret_key = "super secret"
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

cred = credentials.Certificate('firebase_admin_secret.json')
firebase = initialize_app(cred)


@app.post('/device/alert')
def alert():
    return {'message': 'success'}


@app.post('/app/login')
def app_login():
    pass


@app.get('/login')
def get_login():
    return render_template('login.html')


@app.post('/login')
def post_login():
    try:
        user = auth.get_user_by_email(request.form['email'])
        if not user.email_verified or user.disabled:
            raise ValueError('Email not verified or user disabled')
    except FirebaseError as e:
        flash(str(e))
        return redirect(url_for('get_login'))
    except ValueError as e:
        flash(str(e))
        return redirect(url_for('get_login'))
    session['user'] = user.__dict__
    return redirect(url_for('get_dashboard'))


@app.get('/register')
def get_register():
    return render_template('register.html')


@app.post('/register')
def post_register():
    try:
        user = auth.create_user(
            email=request.form['email'],
            email_verified=True,
            password=request.form['password'],
            display_name=request.form['name'],
            disabled=False)
    except FirebaseError as e:
        flash(str(e))
        return redirect(url_for('get_register'))
    except ValueError as e:
        flash(str(e))
        return redirect(url_for('get_register'))
    return redirect(url_for('get_login'))


@app.get('/dashboard')
def get_dashboard():
    if 'user' not in session:
        return redirect(url_for('get_login'))
    return render_template('dashboard.html', user=session['user']['_data'])


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5678, debug=True)
