import os

if __name__ == '__main__':
    from firebase_admin import credentials, initialize_app, auth
    os.system("pwd")
    cred = credentials.Certificate('firebase_admin_secret.json')
    firebase = initialize_app(cred, {
        'databaseURL': 'https://iot-fire-alert-default-rtdb.asia-southeast1.firebasedatabase.app/'
    })

    # user = auth.create_user(
    #     email='user@example.com',
    #     email_verified=False,
    #     password='secretPassword',
    #     display_name='John Doe',
    #     disabled=False)
    # print('Sucessfully created new user: {0}'.format(user.uid))

    user = auth.get_user_by_email('user@example.com')
    print(user.__dict__)