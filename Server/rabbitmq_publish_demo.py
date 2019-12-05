import pika
import sys

connection = pika.BlockingConnection(
    pika.ConnectionParameters('192.168.176.135',
    credentials=pika.PlainCredentials('admin','admin'))
)
channel = connection.channel()

channel.exchange_declare(exchange='logs', exchange_type='fanout')

message = ' '.join(sys.argv[1:]) or "info: Hello World2!"
channel.basic_publish(exchange='logs', routing_key='', body=message)
print(" [x] Sent %r" % message)
connection.close()