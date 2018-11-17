package com.bczl.web.action;

/**
 * 缓存策略
 * <p>
 * Created by licheng1 on 2017/4/5.
 */
public class CacheStrategy {

    private boolean isCached = false;

    private String[] paramKeys;

    private int expire = 60 << 1;

    public static final CacheStrategy NO_CACHED = new CacheStrategy(false);

    public static final CacheStrategy PATH_CACHED = new CacheStrategy(true);

    private CacheStrategy(boolean isCached, String... paramKeys) {
        this.isCached = isCached;
        this.paramKeys = paramKeys;
    }

    private CacheStrategy(boolean isCached, int expire, String... paramKeys) {
        this(isCached, paramKeys);
        this.expire = expire;
    }

    public static CacheStrategy build(String... paramKeys) {
        return new CacheStrategy(true, paramKeys);
    }

    public static CacheStrategy build(int expire, String... paramKeys) {
        return new CacheStrategy(true, expire, paramKeys);
    }

    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }

    public String[] getParamKeys() {
        return paramKeys;
    }

    public void setParamKeys(String[] paramKeys) {
        this.paramKeys = paramKeys;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
