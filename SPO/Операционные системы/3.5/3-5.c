#include <queue>

static std::queue<int> *_threads_queue;
static int _timeslice;
static int _ticked;
static int _current_thread_id;

void switch_to_next_thread()
{
    _ticked = 0;

    if (_threads_queue->size())
    {
        _current_thread_id = _threads_queue->front();
        _threads_queue->pop();
    }
    else
    {
        _current_thread_id = -1;
    }
}

/**
 * Функция будет вызвана перед каждым тестом, если вы
 * используете глобальные и/или статические переменные
 * не полагайтесь на то, что они заполнены 0 - в них
 * могут храниться значения оставшиеся от предыдущих
 * тестов.
 *
 * timeslice - квант времени, который нужно использовать.
 * Поток смещается с CPU, если пока он занимал CPU функция
 * timer_tick была вызвана timeslice раз.
 **/
void scheduler_setup(int timeslice)
{
    _threads_queue = new std::queue<int>();
    _timeslice = timeslice;
    _ticked = 0;
    _current_thread_id = -1;
}

/**
 * Функция вызывается, когда создается новый поток управления.
 * thread_id - идентификатор этого потока и гарантируется, что
 * никакие два потока не могут иметь одинаковый идентификатор.
 **/
void new_thread(int thread_id)
{
    if (_current_thread_id == -1)
    {
        _current_thread_id = thread_id;
    }
    else
    {
        _threads_queue->push(thread_id);
    }
}

/**
 * Функция вызывается, когда поток, исполняющийся на CPU,
 * завершается. Завершится может только поток, который сейчас
 * исполняется, поэтому thread_id не передается. CPU должен
 * быть отдан другому потоку, если есть активный
 * (незаблокированный и незавершившийся) поток.
 **/
void exit_thread()
{
    switch_to_next_thread();
}

/**
 * Функция вызывается, когда поток, исполняющийся на CPU,
 * блокируется. Заблокироваться может только поток, который
 * сейчас исполняется, поэтому thread_id не передается. CPU
 * должен быть отдан другому активному потоку, если таковой
 * имеется.
 **/
void block_thread()
{
    switch_to_next_thread();
}

/**
 * Функция вызывается, когда один из заблокированных потоков
 * разблокируется. Гарантируется, что thread_id - идентификатор
 * ранее заблокированного потока.
 **/
void wake_thread(int thread_id)
{
    if (_current_thread_id == -1)
    {
        _current_thread_id = thread_id;
    }
    else
    {
        _threads_queue->push(thread_id);
    }
}

/**
 * Ваш таймер. Вызывается каждый раз, когда проходит единица
 * времени.
 **/
void timer_tick()
{
    if (_current_thread_id == -1)
    {
        return;
    }

    if (++_ticked == _timeslice)
    {
        _threads_queue->push(_current_thread_id);
        switch_to_next_thread();
    }
}

/**
 * Функция должна возвращать идентификатор потока, который в
 * данный момент занимает CPU, или -1 если такого потока нет.
 * Единственная ситуация, когда функция может вернуть -1, это
 * когда нет ни одного активного потока (все созданные потоки
 * либо уже завершены, либо заблокированы).
 **/
int current_thread()
{
    return _current_thread_id;
}