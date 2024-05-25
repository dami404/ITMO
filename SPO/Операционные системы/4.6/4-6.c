struct lock;
void lock_init(struct lock *lock);
void lock(struct lock *lock);
void unlock(struct lock *lock);

struct condition;
void condition_init(struct condition *cv);
void wait(struct condition *cv, struct lock *lock);
void notify_one(struct condition *cv);
void notify_all(struct condition *cv);

struct wdlock_ctx;

struct wdlock {

    struct wdlock *next;


    const struct wdlock_ctx *owner;


    struct lock lock;
    struct condition cv;
};


struct wdlock_ctx {
    unsigned long long timestamp;
    struct wdlock *locks;
};


void wdlock_ctx_init(struct wdlock_ctx *ctx)
{
    static atomic_ullong next;

    ctx->timestamp = atomic_fetch_add(&next, 1) + 1;
    ctx->locks = NULL;
}

void wdlock_init(struct wdlock *lock)
{
    lock_init(&lock->lock);
    condition_init(&lock->cv);
    lock->owner = NULL;
}

int wdlock_lock(struct wdlock *l, struct wdlock_ctx *ctx)
{

    lock(&l->lock);

    while (l->owner != NULL) {
        if (l->owner->timestamp <= ctx->timestamp) {
            unlock(&l->lock);
            return 0;
        }

        wait(&l->cv, &l->lock);
    }

    l->owner = ctx;
    l->next = ctx->locks;
    ctx->locks = l;
    unlock(&l->lock);

    return 1;
}

void wdlock_unlock(struct wdlock_ctx *ctx)
{

    while (ctx->locks != NULL) {
        struct wdlock *tmp = ctx->locks;

        lock(&tmp->lock);
        tmp->owner = NULL;
        ctx->locks = tmp->next;
        tmp->next = NULL;
        notify_all(&tmp->cv);
        unlock(&tmp->lock);
    }
}