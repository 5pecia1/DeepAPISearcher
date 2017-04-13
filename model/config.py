# config.py

import tensorflow as tf
import os

# size
tf.app.flags.DEFINE_string('encoder_character_max_size', 100, 'encoder charater max size')
tf.app.flags.DEFINE_string('decoder_character_max_size', 150, 'decoder charater max size')
tf.app.flags.DEFINE_integer('encoder_word_vec_size', 300, 'google word2vec embedding size')

tf.app.flags.DEFINE_integer('batch_size', 2, 'batch size(this batch size is even)')
tf.app.flags.DEFINE_integer('epochs', 10, 'epoch number size')

# word2vec ------------------------------------------------------------------------------------

# word2vec dic
tf.app.flags.DEFINE_string('word2vec_dir', '../word2vec', 'word2vec model store')
tf.app.flags.DEFINE_string('encoder_model', 'encoder_model', 'encoder model name')
tf.app.flags.DEFINE_string('decoder_model', 'decoder_model', 'decoder model name')

# word2vec size
tf.app.flags.DEFINE_integer('word2vec_embedding', 300, 'word2vec embedding size')

# word2vec encoder model information
tf.app.flags.DEFINE_integer('encoder_window_size', 5, 'encoder model window size (5~10)')
tf.app.flags.DEFINE_integer('encoder_min_count', 1, 'encoder model') # 지금은 모델내용이 별로 없어서 1로 잡고 나중에 5정도로 생각하고 있음
tf.app.flags.DEFINE_integer('encoder_make_worker', 4, 'make encoder model worker')

# word2vec decoder model information
tf.app.flags.DEFINE_integer('decoder_window_size', 5, 'decoder model window size (5~10)')
tf.app.flags.DEFINE_integer('decoder_min_count', 1, 'decoder model') # 지금은 모델내용이 별로 없어서 1로 잡고 나중에 5정도로 생각하고 있음
tf.app.flags.DEFINE_integer('decoder_make_worker', 4, 'make decoder model worker')


tf.app.flags.DEFINE_string('train_data_dir', '../data', 'train data set dic')

# word2vec dic
tf.app.flags.DEFINE_string('word2vec_dic', 'D:\word2vec\google\GoogleNews-vectors-negative300.bin.gz', 'word2vec dic')
FLAGS = tf.app.flags.FLAGS