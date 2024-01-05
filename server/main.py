from firebase_admin.exceptions import FirebaseError
from flask import Flask, render_template, request, redirect, url_for, flash, session
from flask_session import Session
from firebase_admin import credentials, auth, initialize_app, messaging

app = Flask(__name__)
app.secret_key = "super secret"
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

cred = credentials.Certificate('firebase_admin_secret.json')
firebase = initialize_app(cred)


@app.get('/device/alert')
def alert():
    def get_token(user):
        return 'f_EuDd4kSsiM7smO1IZHKh:APA91bG3MmQRv5F68hLF-8RpDFEM-XxMJ3eO7-90_HXinGGUaZYOfAWB8EOSh8rFPwoCtIOYnlX3pT-4tY53eDeMyasB0uqlR3heQAtFXSG_9lVabHe2e8qx7h4XuYc2aHQlxQYjj8pw'

    user = request.form.get('user')
    registration_token = get_token(user)

    message = messaging.Message(
        data={
            'title': 'dit me chay roi',
            'body': 'dit me chay roi\n'
                    'dia chi: 123 abc\n'
                    'thoi gian: 12:00 12/12/2021\n'
        },
        token=registration_token,
    )

    response = messaging.send(message)

    return {'message': response}


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


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5678, debug=True)
