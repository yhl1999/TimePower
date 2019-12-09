#!/usr/bin/env python
import pika

connection = pika.BlockingConnection(
    pika.ConnectionParameters('121.36.56.36',
    credentials=pika.PlainCredentials('timepower','timepower'))
)
channel = connection.channel()

channel.exchange_declare(exchange='team_c8aa032b-d4d1-4552-b0ca-75970f654079', exchange_type='fanout')

result = channel.queue_declare(queue='', exclusive=True)
queue_name = result.method.queue

channel.queue_bind(exchange='team_c8aa032b-d4d1-4552-b0ca-75970f654079', queue=queue_name)

print(' [*] Waiting for team_c8aa032b-d4d1-4552-b0ca-75970f654079. To exit press CTRL+C')

def callback(ch, method, properties, body):
    print(body)

channel.basic_consume(
    queue=queue_name, on_message_callback=callback, auto_ack=True)

channel.start_consuming()