import io
import os
import secrets
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import func
from PIL import Image
from flask import Flask, request, render_template, send_from_directory, jsonify
from werkzeug.utils import secure_filename
import cv2
import pytesseract

pytesseract.pytesseract.tesseract_cmd = r"C:\\Program Files\\Tesseract-OCR\\tesseract.exe"

ALLOWED_EXTENSIONS = {'bmp', 'png', 'jpg', 'jpeg', 'gif'}
basedir = os.path.abspath(os.path.dirname(__file__))

app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = 'static/screenshots'
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///' + os.path.join(basedir, 'database.db')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

CORS(app, resources={r'/*': {'origins': '*'}})


class Screenshot(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    url = db.Column(db.String(20), nullable=False)
    path = db.Column(db.String(100), nullable=False)
    views = db.Column(db.Integer, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), server_default=func.now())

    def __repr__(self):
        return f'<Screenshot /{self.url}>'


@app.route('/favicon.ico')
def favicon():
    return send_from_directory(os.path.join(basedir, 'static'), 'favicon.ico')


@app.route('/')
@app.route('/home')
def home():
    return render_template('home.html')


@app.route('/v/<string:url>')
def get_screenshot_file(url):
    screenshot = Screenshot.query.filter_by(url=url).first()
    if (screenshot != None):
        screenshot.views += 1
        db.session.commit()
        return send_from_directory(app.config['UPLOAD_FOLDER'], f"{url}.png", as_attachment=True)
    else:
        return jsonify('404')


@app.route('/api/v1/upload/', methods=['GET', 'POST'])
def file_upload():
    if request.method == 'POST':
        image = Image.open(io.BytesIO(request.data))
        image_format = image.format.lower()

        if not (allowed_file(image_format)):
            return jsonify('404')

        random_url = secure_filename(secrets.token_urlsafe(8))

        path = 'flaskServer/' + app.config['UPLOAD_FOLDER'] + f"/{random_url}.{image_format}"
        image.save(path)

        new_screenshot = Screenshot(url=random_url,
                                    path=path,
                                    views=0)

        db.session.add(new_screenshot)
        db.session.commit()
        return f"http://127.0.0.1:8080/v/{random_url}"
    else:
        return jsonify('404')

# https://towardsdatascience.com/read-text-from-image-with-one-line-of-python-code-c22ede074cac
@app.route('/api/v1/read/', methods=['GET', 'POST'])
def read_text():
    if request.method == 'POST':
        image = Image.open(io.BytesIO(request.data))
        image_format = image.format.lower()

        random_url = secure_filename(secrets.token_urlsafe(16))

        if not (allowed_file(image_format)):
            return render_template('404.html')

        path = 'flaskServer/' + app.config['UPLOAD_FOLDER'] + f"/{random_url}.{image_format}"
        image.save(path)

        read_image = cv2.imread(path)
        text = pytesseract.image_to_string(read_image)

        os.remove(path)

        return text
    else:
        return render_template('404.html')


# sanity check route
@app.route('/ping', methods=['GET'])
def ping_pong():
    return jsonify('pong!')


def allowed_file(filename: str):
    if filename.lower() in ALLOWED_EXTENSIONS:
        return True
    else:
        return False


if __name__ == '__main__':
    app.run()
