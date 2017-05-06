# data_helper.py    `
import os
import nltk
import re
from config import FLAGS
import numpy as np


class MySentences(object):
    def __init__(self, dirname):
        self.dirname = dirname

    def __iter__(self):
        for fname in os.listdir(self.dirname):
            for line in open(os.path.join(self.dirname, fname), encoding='utf-8'):
                yield line.strip()


class DataPreProcessing(object):
    def __init__(self, file_dir):
        self.data_set = {}
        self.file_dir = file_dir
        self.pad = '_PAD_'  # padding tag
        self.go = '_GO_'  # decoder sentence first tag
        self.eos = '_EOS_'  # decoder sentence last tag
        self.ukn = '_UKN_'  # unknown
        self.tag_list = [self.pad, self.go, self.eos, self.ukn]

        self.sentences = MySentences(self.file_dir)
        self.sentences = [sentence for sentence in self.sentences]
        self.__make_data_set(self.sentences)

    def tokenization(self):
        self.__tokenization()
        return self.data_set

    def make_data_set_seq2seq(self):
        self.__tokenization()
        self.__padding()
        return self.data_set

    def __make_data_set(self, sentence):
        for i in range(len(sentence)):
            if i % 2 == 0:
                self.data_set.setdefault('q', []).append(self.__clean_str(sentence[i]))
            else:
                self.data_set.setdefault('a', []).append(sentence[i])

    def __tokenization(self):
        key_list = self.data_set.keys()

        for key_name in key_list:
            temp=[]
            if key_name == 'q':
                for line in self.data_set[key_name]:
                    temp.append(np.array(nltk.word_tokenize(line)))
            elif key_name == 'a':
                for line in self.data_set[key_name]:
                    temp.append(np.array(line.split('-')))

            self.data_set[key_name] = temp

    def __padding(self):
        for key_name in self.data_set.keys():
            sentence = self.data_set[key_name]
            for i, character_list in enumerate(sentence):
                if key_name == 'q':
                    remainder = FLAGS.encoder_character_max_size - len(character_list)
                    pad_list = [['_PAD_'] for _ in range(remainder)]
                    self.data_set[key_name][i] = np.append(character_list, pad_list)[::-1]

                elif key_name == 'a':
                    remainder = FLAGS.encoder_character_max_size - len(character_list) - 2
                    pad_list = [['_PAD_'] for _ in range(remainder)]
                    eos = np.append(character_list, ['_EOS_'])
                    character_list = np.append(['_GO_'], eos)
                    self.data_set[key_name][i] = np.append(character_list, pad_list)

    def __stemming(self):
        """
        stemming
        :return: 
        """
        pass

    def apply_word_to_vector(self, encoder_model, decoder_model):
        key_list = self.data_set.keys()
        for key_name in key_list:
            for i, sentence in enumerate(self.data_set[key_name]):
                if key_name == 'q':
                    self.__sentence_to_vector(sentence, encoder_model)
                    pass
                elif key_name == 'a':
                    # self.__sentence_to_vector(sentence, decoder_model)
                    pass

    def __sentence_to_vector(self, sentence, model):
        for word in sentence:
            if not word in self.tag_list:
                try:
                    pass
                except:
                    print('error {}'.format(word))

    def batch_iter(self, epochs, batch_size):
        total_data_size = len(self.data_set['q'])
        par_number = int(total_data_size / batch_size) + 1

        q_sentence_list = self.data_set['q']
        a_sentence_list = self.data_set['a']

        for epoch in range(epochs):
            for par in range(par_number):
                start = par * batch_size
                end = min((par+1) * batch_size, total_data_size)
                yield q_sentence_list[start:end], a_sentence_list[start:end]

    def __clean_str(self, str):
        """
        Tokenization/string cleaning for all datasets except for SST.
        Original taken from https://github.com/yoonkim/CNN_sentence/blob/master/process_data.py
        """
        str = re.sub(r"[^A-Za-z0-9(),!?\'\`]", " ", str)
        str = re.sub(r"\'s", " \'s", str)
        str = re.sub(r"\'ve", " \'ve", str)
        str = re.sub(r"n\'t", " n\'t", str)
        str = re.sub(r"\'re", " \'re", str)
        str = re.sub(r"\'d", " \'d", str)
        str = re.sub(r"\'ll", " \'ll", str)
        str = re.sub(r",", " , ", str)
        str = re.sub(r"!", " ! ", str)
        str = re.sub(r"\(", " \( ", str)
        str = re.sub(r"\)", " \) ", str)
        str = re.sub(r"\?", " \? ", str)
        str = re.sub(r"\s{2,}", " ", str)
        return str.strip().lower()

if __name__ == '__main__':
    pass