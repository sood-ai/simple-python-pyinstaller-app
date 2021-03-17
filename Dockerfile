FROM registry.redhat.io/rhel8/python-38

RUN mkdir app 
COPY ./app.py /app/app.py

RUN pip install flask

CMD [ "python", "/app/app.py" ]
