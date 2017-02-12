from flask import Flask,json
from flask.ext.mysql import MySQL
import os
import credentials

app = Flask(__name__)
mysql = MySQL()

# MySQL configurations
app.config['MYSQL_DATABASE_USER'] = os.environ['MYSQL_DATABASE_USER']
app.config['MYSQL_DATABASE_PASSWORD'] = os.environ['MYSQL_DATABASE_PASSWORD']
app.config['MYSQL_DATABASE_DB'] = os.environ['MYSQL_DATABASE_DB']
app.config['MYSQL_DATABASE_HOST'] = os.environ['MYSQL_DATABASE_HOST']
mysql.init_app(app)


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
        pass


if __name__ == '__main__':
    app.run()
