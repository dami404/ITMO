#include <inttypes.h>
#include <stdlib.h>
/**
 * Эти две функции вы должны использовать для аллокации
 * и освобождения памяти в этом задании. Считайте, что
 * внутри они используют buddy аллокатор с размером
 * страницы равным 4096 байтам.
 **/

/**
 * Аллоцирует участок размером 4096 * 2^order байт,
 * выровненный на границу 4096 * 2^order байт. order
 * должен быть в интервале [0; 10] (обе границы
 * включительно), т. е. вы не можете аллоцировать больше
 * 4Mb за раз.
 **/
void *alloc_slab(int order);
/**
 * Освобождает участок ранее аллоцированный с помощью
 * функции alloc_slab.
 **/
void free_slab(void *slab);

typedef struct memory_block
{
    struct memory_block *next;
} block_mem_t;

typedef struct slab_header
{
    block_mem_t *blocks;
    size_t free_num;
    struct slab_header *next;
    struct slab_header *prev;
} slab_head_t;

#define SLAB_OBJECTS_MIN_NUM 100

/**
 * Эта структура представляет аллокатор, вы можете менять
 * ее как вам удобно. Приведенные в ней поля и комментарии
 * просто дают общую идею того, что вам может понадобится
 * сохранить в этой структуре.
 **/
struct cache {
    slab_head_t *full;
    slab_head_t *partly_full;
    slab_head_t *free;

    size_t object_size; /* размер аллоцируемого объекта */
    int slab_order; /* используемый размер SLAB-а */
    size_t slab_objects; /* количество объектов в одном SLAB-е */ 
};

static void chache_init_slab(slab_head_t *slab, size_t object_size, size_t objects_num)
{
    void *mem = (uint8_t*)slab + sizeof(slab_head_t);
    size_t offset = sizeof(block_mem_t) + object_size;

    for (int i = 0; i < objects_num; ++i)
    {
        ((block_mem_t*)mem)->next = (i + 1) == objects_num ? NULL : (block_mem_t*)((uint8_t*)mem + offset);
        mem = (uint8_t*)mem + offset;
    }

    slab->blocks = (block_mem_t*)((uint8_t*)slab + sizeof(slab_head_t));
    slab->free_num = objects_num;
}

static slab_head_t *cache_create_slab(int order)
{
    slab_head_t *slab;

    slab = (slab_head_t*)alloc_slab(order);
    
    slab->blocks = NULL;
    slab->free_num = 0;
    slab->next = NULL;

    return slab;
}

/**
 * Функция инициализации будет вызвана перед тем, как
 * использовать это кеширующий аллокатор для аллокации.
 * Параметры:
 *  - cache - структура, которую вы должны инициализировать
 *  - object_size - размер объектов, которые должен
 *    аллоцировать этот кеширующий аллокатор 
 **/
void cache_setup(struct cache *cache, size_t object_size)
{
    int order = 0;
    size_t meta_size = sizeof(slab_head_t);

    cache->full = NULL;
    cache->partly_full = NULL;
    cache->free = NULL;
    cache->object_size = object_size;

    size_t min_mem_required = sizeof(slab_head_t) + (sizeof(block_mem_t) + object_size) * SLAB_OBJECTS_MIN_NUM;
    while (((1UL << order) * 4096) < min_mem_required)
    {
        ++order;
    }

    cache->slab_order = order;

    size_t addition_mem = ((1UL << order) * 4096 / min_mem_required - 1) * min_mem_required +
                          ((1UL << order) * 4096 % min_mem_required);

    cache->slab_objects = SLAB_OBJECTS_MIN_NUM + addition_mem / (sizeof(block_mem_t) + object_size);
}

/**
 * Функция освобождения будет вызвана когда работа с
 * аллокатором будет закончена. Она должна освободить
 * всю память занятую аллокатором. Проверяющая система
 * будет считать ошибкой, если не вся память будет
 * освбождена.
 **/
void cache_release(struct cache *cache)
{
    slab_head_t *tmp;

    tmp = cache->full;
    while (tmp)
    {
        cache->full = tmp->next;
        free_slab(tmp);
        tmp = cache->full;
    }

    tmp = cache->partly_full;
    while (tmp)
    {
        cache->partly_full = tmp->next;
        free_slab(tmp);
        tmp = cache->partly_full;
    }

    tmp = cache->free;
    while (tmp)
    {
        cache->free = tmp->next;
        free_slab(tmp);
        tmp = cache->free;
    }
}

static void cache_move_slab(struct cache *cache, slab_head_t **dest, slab_head_t *src)
{
    if (cache->full == src)
        cache->full = src->next;
    else if (cache->partly_full == src)
        cache->partly_full = src->next;
    else if (cache->free == src)
        cache->free = src->next;

    if (src->prev)
    {
        src->prev->next = src->next;
    }
    if (src->next)
    {
        src->next->prev = src->prev;
    }

    src->prev = NULL;
    src->next = *dest;
    if (*dest)
        (*dest)->prev = src;
    *dest = src;
}

static void cache_free_block(slab_head_t *slab, block_mem_t *block)
{
    block->next = slab->blocks;
    slab->blocks = block;

    ++slab->free_num;
}

static block_mem_t *cache_alloc_block(slab_head_t *slab)
{
    block_mem_t *block;
    static size_t i = 0;

    block = slab->blocks;
    slab->blocks = block->next;
    --slab->free_num;

    block->next = NULL; // do we need it actually?

    return block;
}

/**
 * Функция аллокации памяти из кеширующего аллокатора.
 * Должна возвращать указатель на участок памяти размера
 * как минимум object_size байт (см cache_setup).
 * Гарантируется, что cache указывает на корректный
 * инициализированный аллокатор.
 **/
void *cache_alloc(struct cache *cache)
{
    block_mem_t *block;

    if (cache->partly_full)
    {
        block = cache_alloc_block(cache->partly_full);
        
        if (cache->partly_full->free_num == 0)
        {
            cache_move_slab(cache, &cache->full, cache->partly_full);
        }
    }
    else if (cache->free)
    {
        block = cache_alloc_block(cache->free);
        cache_move_slab(cache, &cache->partly_full, cache->free);
    }
    else
    {
        slab_head_t *slab = cache_create_slab(cache->slab_order);
        
        chache_init_slab(slab, cache->object_size, cache->slab_objects);

        block = cache_alloc_block(slab);
        cache->partly_full = slab;
    }

    return (uint8_t*)block + sizeof(block_mem_t);
}


/**
 * Функция освобождения памяти назад в кеширующий аллокатор.
 * Гарантируется, что ptr - указатель ранее возвращенный из
 * cache_alloc.
 **/
void cache_free(struct cache *cache, void *ptr)
{
    slab_head_t *slab = (slab_head_t*)((uint64_t)ptr & ~((1UL << cache->slab_order) * 4096 - 1));
    block_mem_t *block = (block_mem_t*)((uint8_t*)ptr - sizeof(block_mem_t));

    cache_free_block(slab, block);

    if (slab->free_num == 1)
    {
        cache_move_slab(cache, &cache->partly_full, slab);
    }
    else if (slab->free_num == cache->slab_objects)
    {
        cache_move_slab(cache, &cache->free, slab);
    }
}


/**
 * Функция должна освободить все SLAB, которые не содержат
 * занятых объектов. Если SLAB не использовался для аллокации
 * объектов (например, если вы выделяли с помощью alloc_slab
 * память для внутренних нужд вашего алгоритма), то освбождать
 * его не обязательно.
 **/
void cache_shrink(struct cache *cache)
{
    slab_head_t *tmp;

    tmp = cache->free;
    while (tmp)
    {
        cache->free = tmp->next;
        free_slab(tmp);
        tmp = cache->free;
    }
}