from flask import json
import collections
from flask.ext.mysql import MySQL
import os
import credentials
import bcrypt
from flask import Flask, request, redirect, url_for, send_file
from werkzeug.utils import secure_filename

import hashlib, uuid

app = Flask(__name__)
mysql = MySQL()

# MySQL configurations
app.config['MYSQL_DATABASE_USER'] = os.environ['MYSQL_DATABASE_USER']
app.config['MYSQL_DATABASE_PASSWORD'] = os.environ['MYSQL_DATABASE_PASSWORD']
app.config['MYSQL_DATABASE_DB'] = os.environ['MYSQL_DATABASE_DB']
app.config['MYSQL_DATABASE_HOST'] = os.environ['MYSQL_DATABASE_HOST']
mysql.init_app(app)

# Image folder configuration
UPLOAD_FOLDER = 'image/'
CURRENT_DIRECTORY = os.path.dirname(os.path.abspath(__file__)) + '/'
JPG_EXT = ".jpg"
ALLOWED_EXTENSIONS = set(['jpg', 'jpeg', 'JPG', 'JPEG'])  # 'png', 'gif'
DEFAULT_IMG = CURRENT_DIRECTORY + 'default-img' + JPG_EXT


@app.route('/')
def hello_world():
    return 'Hello World!'

@app.route('/users/<id>')
def get_user(id):
    conn = mysql.connect()
    cur = conn.cursor()
    query = "SELECT id, firstname, lastname, countryIsoAlpha2, city, dob, email FROM users WHERE id = %s"
    cur.execute(query, id)
    data = cur.fetchone()
    conn.close()

    if data is not None:
        return json.jsonify({"id": data[0],
                             "firstname": data[1],
                             "lastname": data[2],
                             "countryIsoAlpha2" : data[3],
                             "city" : data[4],
                             "dob" : data[5],
                             "email" : data[6]
                             }
                            )
    else:
        return json.jsonify({}),404

@app.route('/login')
def login():
    content = request.get_json()
    if (isUserExist(content["email"])):
        conn = mysql.connect()
        cur = conn.cursor()
        cur.execute("SELECT id, password FROM users WHERE email = %s", (content["email"]))
        data = cur.fetchone()[1]
        password = content["password"].encode('utf-8')
        if bcrypt.hashpw(password, data) == data:
            get_user(cur.fetchone()[0])
            conn.close()
    else:
        return 'This email does not exist'

@app.route('/usersWithoutBuddies/')
def get_user_without_buddies():
    conn = mysql.connect()
    cur = conn.cursor()
    query = "SELECT id, firstname, lastname, countryIsoAlpha2, city, dob, email FROM users WHERE NOT EXISTS" \
            " (SELECT * FROM buddies WHERE id1 = id OR id2 = id)"
    cur.execute(query)

    data = cur.fetchall()
    conn.close()

    objects_list = []
    for row in data:
        d = collections.OrderedDict()
        d['id'] = row[0]
        d['firstname'] = row[1],
        d['lastname'] = row[2],
        d['countryIsoAlpha2'] = row[3],
        d['city'] = row[4],
        d['dob'] = row[5],
        d['email'] = row[6]
        objects_list.append(d)

    returnList = collections.OrderedDict()
    returnList['listOfUsers'] = objects_list

    return json.jsonify(returnList)



@app.route('/savePicture/<userId>/', methods=['POST'])
def savePicture(userId):

    if not userId or userId.isspace():
        return 'User id is empty!',500

    if 'imagefile' not in request.files:
        return 'No image send in the POST request.',500

    file = request.files['imagefile']

    file.filename = userId + JPG_EXT
    file.save(CURRENT_DIRECTORY + UPLOAD_FOLDER + file.filename)

    return "",200

# GET PICTURE
@app.route('/getPicture/<id>/')
def getPicture(id):

    filename = CURRENT_DIRECTORY + UPLOAD_FOLDER + id + JPG_EXT

    if os.path.isfile(filename):
        return send_file(filename, mimetype='image/jpeg')
    else:
        print 'Failed to get image "' + id + '". Return default image.'
    return send_file(DEFAULT_IMG, mimetype='image/jpeg')


@app.route('/user', methods=['POST'])
def create_user():
    content = request.get_json()
    if (isUserExist(content["email"])):
        return 'This email has already been used !!!'
    else:
        conn = mysql.connect()
        cur = conn.cursor()
        password = encrypt(content["password"])
        cur.execute("INSERT INTO users (firstname, lastname, countryIsoAlpha2, city, dob, email, password) VALUES ( %s, %s, %s, %s, %s, %s, %s )", ( content["firstname"], content["lastname"], content["countryIsoAlpha2"], content["city"], content["dob"], content["email"], password ) )
        conn.commit()
        conn.close()
        return 'Account has been created successfully'


def isUserExist(email):
    conn = mysql.connect()
    cur = conn.cursor()
    cur.execute("SELECT email FROM users WHERE email = %s ", (email))
    data = cur.fetchone()
    conn.close()

    if data is None:
        return False
    else:
        return True

def encrypt(password):
    pss = password.encode('utf-8')
    hashed = bcrypt.hashpw(pss, bcrypt.gensalt())
    return hashed

if __name__ == '__main__':
    app.run()
