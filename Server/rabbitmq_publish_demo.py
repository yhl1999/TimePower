import pika
import sys

connection = pika.BlockingConnection(
    pika.ConnectionParameters('121.36.56.36',
    credentials=pika.PlainCredentials('timepower','timepower'))
)
channel = connection.channel()

channel.exchange_declare(exchange='team_9d8075c7-bba4-4604-b3ab-f93b8af5b60e', exchange_type='fanout')

message = ' '.join(sys.argv[1:]) or "total_Lyh&ningyuv"
channel.basic_publish(exchange='team_9d8075c7-bba4-4604-b3ab-f93b8af5b60e', routing_key='', body=message)
print(" [x] Sent %r" % message)
connection.close()