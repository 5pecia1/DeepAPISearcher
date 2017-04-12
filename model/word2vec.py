# word2vec.py
from gensim.models import Word2Vec
import gensim
from data_helper import DataPreProcessing
from config import FLAGS
import os

class word2vec(object):
    def __init__(self):
        self.encoder_model_dir = os.path.join(FLAGS.word2vec_dir, FLAGS.encoder_model)
        self.decoder_model_dir = os.path.join(FLAGS.word2vec_dir, FLAGS.decoder_model)
        self.data_set = {}
        self.encoder_model = None
        self.decoder_model = None

    def load_data_set(self, file_dir):
        preprocessing = DataPreProcessing(file_dir)
        self.data_set = preprocessing.tokenization()

    def make_encoder_word2vec(self):
        if self.data_set == {}:
            print('not load data set')
            return

        encoder_sentence_list = self.data_set['q']
        for i, sentence in enumerate(encoder_sentence_list):
            encoder_sentence_list[i] = sentence.tolist()

        self.encoder_model = gensim.models.Word2Vec(sentences=encoder_sentence_list,
                                                    size=FLAGS.word2vec_embedding,
                                                    window=FLAGS.encoder_window_size,
                                                    min_count=FLAGS.encoder_min_count,
                                                    workers=FLAGS.encoder_make_worker)
        # save encoder model
        self.encoder_model.save(self.encoder_model_dir)

    def make_decoder_word2vec(self):
        if self.data_set == {}:
            print('not load data set')
            return

        decoder_sentence_list = self.data_set['a']
        for i, sentence in enumerate(decoder_sentence_list):
            decoder_sentence_list[i] = sentence.tolist()

        self.decoder_model = gensim.models.Word2Vec(sentences=decoder_sentence_list,
                                                    size=FLAGS.word2vec_embedding,
                                                    window=FLAGS.decoder_window_size,
                                                    min_count=FLAGS.decoder_min_count,
                                                    workers=FLAGS.decoder_make_worker)

        # save decoder model
        self.decoder_model.save(self.decoder_model_dir)

    def get_encoder_model(self):
        if not os.path.exists(self.encoder_model_dir):
            print('error!! not exists {} model file'.format(self.encoder_model_dir))
            return

        self.encoder_model = Word2Vec.load(self.encoder_model_dir)
        return self.encoder_model

    def get_decoder_model(self):
        if not os.path.exists(self.decoder_model_dir):
            print('error!! not exists {} model file'.format(self.decoder_model_dir))
            return

        self.decoder_model = Word2Vec.load(self.decoder_model_dir)
        return self.decoder_model


if __name__ == '__main__':
    w2v = word2vec()
    w2v.load_data_set('../data')
    w2v.make_encoder_word2vec()
    w2v.make_decoder_word2vec()
    encoder_model = w2v.get_encoder_model()
    decoder_model = w2v.get_decoder_model()
    print(encoder_model['bytes'])
