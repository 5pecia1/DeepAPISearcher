"""
tensorflow 1.0 version
RSTM을 이용한 구
"""
import tensorflow as tf
from tensorflow.contrib.rnn import BasicLSTMCell, static_rnn

CONSTANT = tf.app.flags
CONSTANT.DEFINE_integer("epoch", 1000, "epoch when learning")
CONSTANT.DEFINE_integer("samples", 1000, "number of sample for learning")
CONSTANT.DEFINE_integer("state_size", 100, "state size in rnn")
CONSTANT.DEFINE_integer("recurrent", 200, "number of recurrent hidden layer")
CONSTANT.DEFINE_integer("input_vector_size", 1, "input vector size")
CONSTANT.DEFINE_integer("learning_rate", 0.01, "learning rate for optimizer")
CONSTANT.DEFINE_integer("batch_size", 100, "mini batch size")
CONST = CONSTANT.FLAGS
class sin_RNN(object):
    """
    many to one

    """
    def __init__(self):
        self._gen_sin_data()
        print("generated data")

        self._build_batch()
        print("build_batch")

        self._set_variable()
        print("set variable")

        self._build_model()
        print("build model")

        self._saver_model()
        print("saver created")

        self._build_train()
        print("loss graph created")

        self._initialize()
        print("initialized")

    def training(self):
        """
        * run predication
        """
        print("start traing")
        for step in range(CONST.epoch):
            loss = self._run_train()
            if step % 10 == 0:
                print("step : ", step)
                print("loss : ", loss)


    @classmethod
    def _run_train(cls):
        cls.sess.run(tf.global_variables_initializer())
        _, loss= cls.sess.run([cls.train, cls.loss])
        return loss

    @classmethod
    def _set_variable(cls):
        linear_w = tf.Variable(tf.truncated_normal([CONST.recurrent, CONST.batch_size, 1]))
        linear_b = tf.Variable(tf.zero([CONST.recurrent, 1, 1]))

        cls.linear_w = tf.unstack(linear_w)
        cls.linear_b = tf.unstack(linear_b)


    @classmethod
    def _gen_sin_data(cls):
        ts_X = tf.constant(list(range(CONST.samples + 1)), dtype=tf.float32)
        ts_Y = tf.sin(ts_X * 0.01)

        sz_batch = (int(CONST.samples/CONST.recurrent), CONST.recurrent, CONST.input_vector_size)
        cls.batch_input = tf.reshape(ts_Y[:-1], sz_batch)
        cls.batch_label = tf.reshape(ts_Y[1:], sz_batch)

    @classmethod
    def _build_batch(cls):
        batch_set = [cls.batch_input, cls.batch_label]
        cls.input, cls.label = tf.train.batch(batch_set, CONST.batch_size, enqueue_many=True)

    @classmethod
    def _set_variable(cls):
        linear_w = tf.Variable(tf.truncated_normal([CONST.recurrent, CONST.state_size, 1]))
        linear_b = tf.Variable(tf.zeros([CONST.recurrent, 1, 1]))

        cls.linear_w = tf.unstack(linear_w)
        cls.linear_b = tf.unstack(linear_b)

    @classmethod
    def _saver_model(cls):
        cls.saver = tf.train.Saver()

    @classmethod
    def _build_train(cls):
        cls.loss = 0
        for i in range(CONST.recurrent):
            cls.loss = tf.reduce_mean(tf.pow(cls.pred[i] - cls.label_set[i], 2))

        cls.train = tf.train.GradientDescentOptimizer(CONST.learning_rate).minimize(cls.loss)


    @classmethod
    def _build_model(cls):
        # tf.contrib.rnn.BasicLSTMCell
        # _init__(num_units, forget_bias=1.0, input_size=None, state_is_tuple=True, activation=tf.tanh)
        # num_units : 셀의 단위 갯수 셀한개가 가지는 원소의 갯수
        # forget_bias : forget하는 bias의 개수 뭐때문에 쓰는지는 잘 모름
        # input_size : Deprecated and unused.(더이상 사용되지 않음) 셀이 몇개가 될 것인지는 RNN을 실행할 떄 결정한다.
        # state_is_tuple :
        # activation : 활성화 함수
        rstm_cell = BasicLSTMCell(num_units=CONST.state_size)
        init_state = rstm_cell.zero_state(batch_size=CONST.batch_size, dtype=tf.float32)
        cls.input_set = tf.unstack(cls.input, axis=1)
        cls.label_set = tf.unstack(cls.label, axis=1)
        cls.output, state = static_rnn(cell=rstm_cell,
                                       inputs=cls.input_set,
                                       initial_state=init_state,
                                       dtype=tf.float32)
        cls.pred = tf.matmul(cls.output, cls.linear_w) + cls.linear_b

    @classmethod
    def _initialize(cls):
        cls.sess = tf.Session()
        cls.coord = tf.train.Coordinator()
        cls.thread = tf.train.start_queue_runners(sess=cls.sess, coord=cls.coord)


def main(_):
    rnn = sin_RNN()
    rnn.training()


if __name__ == '__main__':
    tf.app.run()
