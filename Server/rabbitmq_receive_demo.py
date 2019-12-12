#!/usr/bin/env python
import pika

connection = pika.BlockingConnection(
    pika.ConnectionParameters('121.36.56.36',
    credentials=pika.PlainCredentials('timepower','timepower'))
)
channel = connection.channel()

channel.exchange_declare(exchange='team_3bfac96e-f6b1-4c57-85af-773b95684ca6', exchange_type='fanout')

result = channel.queue_declare(queue='', exclusive=True)
queue_name = result.method.queue

channel.queue_bind(exchange='team_3bfac96e-f6b1-4c57-85af-773b95684ca6', queue=queue_name)

print(' [*] Waiting for team_3bfac96e-f6b1-4c57-85af-773b95684ca6. To exit press CTRL+C')

def callback(ch, method, properties, body):
    print(body)

channel.basic_consume(
    queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()