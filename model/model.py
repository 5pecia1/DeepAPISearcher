import tensorflow as tf
from data_helper import MySentences

if __name__ == '__main__':
    sentences = MySentences('../data')
    for sentence in sentences:
        print(sentence)