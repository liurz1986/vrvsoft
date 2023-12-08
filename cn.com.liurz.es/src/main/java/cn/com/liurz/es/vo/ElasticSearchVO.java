package cn.com.liurz.es.vo;

import cn.com.liurz.es.common.ElasticSearchException;
import cn.com.liurz.es.util.ResultCodeEnum;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ElasticSearchVO<T> {
    private String _scroll_id;
    private int took;
    private boolean timed_out;
    private Shards _shards;
    private Hits<T> hits;

    public List<T> getList(Class<T> clazz) {
        List<T> list = new ArrayList();
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<HitsChild<T>> hits = this.getHits().getHits();
        Iterator var5 = hits.iterator();

        while(var5.hasNext()) {
            HitsChild<T> hitsChild = (HitsChild)var5.next();
            T source = hitsChild.get_source();
            String json = gson.toJson(source);
            T t = gson.fromJson(json, clazz);
            list.add(t);
        }

        return list;
    }

    public List<T> getList(Class<T> clazz, String idField) {
        List<T> list = new ArrayList();
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<HitsChild<T>> hits = this.getHits().getHits();
        for(HitsChild<T> hit : hits){
            T source = hit.get_source();
            list.add(source);
            String json = gson.toJson(source);
            T t = gson.fromJson(json, clazz);
            try {
                BeanUtils.setProperty(t, idField, hit.get_id());
            } catch (Exception var12) {
                throw new ElasticSearchException(ResultCodeEnum.ERROR.getCode(), "未配置的PrimaryKey依赖，请配置");
            }
        }
        return list;
    }

    public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList();
        Gson gson = (new GsonBuilder()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<HitsChild<T>> hits = this.getHits().getHits();
        Iterator var4 = hits.iterator();

        while(var4.hasNext()) {
            HitsChild<T> hitsChild = (HitsChild)var4.next();
            T source = hitsChild.get_source();
            String json = gson.toJson(source);
            Map<String, Object> map = (Map)gson.fromJson(json, (new TypeToken<Map<String, Object>>() {
            }).getType());
            list.add(map);
        }

        return list;
    }

    public PageRes<T> toPaginationResponse(Class<T> clazz) {
        PageRes<T> findByPage = new PageRes();
        List<T> listResult = this.getList(clazz);
        findByPage.setList(listResult);
        long value = this.getHits().getTotal().getValue();
        findByPage.setTotal(value);
        findByPage.setCode("0");
        findByPage.setMessage("success");
        return findByPage;
    }

    public PageRes<Map<String, Object>> toPaginationResponse() {
        PageRes<Map<String, Object>> findByPage = new PageRes();
        List<Map<String, Object>> listResult = this.getList();
        findByPage.setList(listResult);
        long value = this.getHits().getTotal().getValue();
        findByPage.setTotal(value);
        findByPage.setCode("0");
        findByPage.setMessage("success");
        return findByPage;
    }

    public ElasticSearchVO() {
    }

    public String get_scroll_id() {
        return this._scroll_id;
    }

    public int getTook() {
        return this.took;
    }

    public boolean isTimed_out() {
        return this.timed_out;
    }

    public Shards get_shards() {
        return this._shards;
    }

    public Hits<T> getHits() {
        return this.hits;
    }

    public void set_scroll_id(final String _scroll_id) {
        this._scroll_id = _scroll_id;
    }

    public void setTook(final int took) {
        this.took = took;
    }

    public void setTimed_out(final boolean timed_out) {
        this.timed_out = timed_out;
    }

    public void set_shards(final Shards _shards) {
        this._shards = _shards;
    }

    public void setHits(final Hits<T> hits) {
        this.hits = hits;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ElasticSearchVO)) {
            return false;
        } else {
            ElasticSearchVO<?> other = (ElasticSearchVO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label55: {
                    Object this$_scroll_id = this.get_scroll_id();
                    Object other$_scroll_id = other.get_scroll_id();
                    if (this$_scroll_id == null) {
                        if (other$_scroll_id == null) {
                            break label55;
                        }
                    } else if (this$_scroll_id.equals(other$_scroll_id)) {
                        break label55;
                    }

                    return false;
                }

                if (this.getTook() != other.getTook()) {
                    return false;
                } else if (this.isTimed_out() != other.isTimed_out()) {
                    return false;
                } else {
                    Object this$_shards = this.get_shards();
                    Object other$_shards = other.get_shards();
                    if (this$_shards == null) {
                        if (other$_shards != null) {
                            return false;
                        }
                    } else if (!this$_shards.equals(other$_shards)) {
                        return false;
                    }

                    Object this$hits = this.getHits();
                    Object other$hits = other.getHits();
                    if (this$hits == null) {
                        if (other$hits != null) {
                            return false;
                        }
                    } else if (!this$hits.equals(other$hits)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ElasticSearchVO;
    }

    public int hashCode() {
        int result = 1;
        Object $_scroll_id = this.get_scroll_id();
        result = result * 59 + ($_scroll_id == null ? 43 : $_scroll_id.hashCode());
        result = result * 59 + this.getTook();
        result = result * 59 + (this.isTimed_out() ? 79 : 97);
        Object $_shards = this.get_shards();
        result = result * 59 + ($_shards == null ? 43 : $_shards.hashCode());
        Object $hits = this.getHits();
        result = result * 59 + ($hits == null ? 43 : $hits.hashCode());
        return result;
    }

    public String toString() {
        return "ElasticSearchVO(_scroll_id=" + this.get_scroll_id() + ", took=" + this.getTook() + ", timed_out=" + this.isTimed_out() + ", _shards=" + this.get_shards() + ", hits=" + this.getHits() + ")";
    }

    public static class HitsChild<T> {
        private String _index;
        private String _type;
        private String _id;
        private float _score;
        private T _source;

        public HitsChild() {
        }

        public String get_index() {
            return this._index;
        }

        public String get_type() {
            return this._type;
        }

        public String get_id() {
            return this._id;
        }

        public float get_score() {
            return this._score;
        }

        public T get_source() {
            return this._source;
        }

        public void set_index(final String _index) {
            this._index = _index;
        }

        public void set_type(final String _type) {
            this._type = _type;
        }

        public void set_id(final String _id) {
            this._id = _id;
        }

        public void set_score(final float _score) {
            this._score = _score;
        }

        public void set_source(final T _source) {
            this._source = _source;
        }

        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ElasticSearchVO.HitsChild)) {
                return false;
            } else {
                HitsChild<?> other = (HitsChild)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label63: {
                        Object this$_index = this.get_index();
                        Object other$_index = other.get_index();
                        if (this$_index == null) {
                            if (other$_index == null) {
                                break label63;
                            }
                        } else if (this$_index.equals(other$_index)) {
                            break label63;
                        }

                        return false;
                    }

                    Object this$_type = this.get_type();
                    Object other$_type = other.get_type();
                    if (this$_type == null) {
                        if (other$_type != null) {
                            return false;
                        }
                    } else if (!this$_type.equals(other$_type)) {
                        return false;
                    }

                    Object this$_id = this.get_id();
                    Object other$_id = other.get_id();
                    if (this$_id == null) {
                        if (other$_id != null) {
                            return false;
                        }
                    } else if (!this$_id.equals(other$_id)) {
                        return false;
                    }

                    if (Float.compare(this.get_score(), other.get_score()) != 0) {
                        return false;
                    } else {
                        Object this$_source = this.get_source();
                        Object other$_source = other.get_source();
                        if (this$_source == null) {
                            if (other$_source != null) {
                                return false;
                            }
                        } else if (!this$_source.equals(other$_source)) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ElasticSearchVO.HitsChild;
        }

        public int hashCode() {
            int result = 1;
            Object $_index = this.get_index();
            result = result * 59 + ($_index == null ? 43 : $_index.hashCode());
            Object $_type = this.get_type();
            result = result * 59 + ($_type == null ? 43 : $_type.hashCode());
            Object $_id = this.get_id();
            result = result * 59 + ($_id == null ? 43 : $_id.hashCode());
            result = result * 59 + Float.floatToIntBits(this.get_score());
            Object $_source = this.get_source();
            result = result * 59 + ($_source == null ? 43 : $_source.hashCode());
            return result;
        }

        public String toString() {
            return "ElasticSearchVO.HitsChild(_index=" + this.get_index() + ", _type=" + this.get_type() + ", _id=" + this.get_id() + ", _score=" + this.get_score() + ", _source=" + this.get_source() + ")";
        }
    }

    public static class Total {
        private long value;
        private String relation;

        public Total() {
        }

        public long getValue() {
            return this.value;
        }

        public String getRelation() {
            return this.relation;
        }

        public void setValue(final long value) {
            this.value = value;
        }

        public void setRelation(final String relation) {
            this.relation = relation;
        }

        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ElasticSearchVO.Total)) {
                return false;
            } else {
                Total other = (Total)o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (this.getValue() != other.getValue()) {
                    return false;
                } else {
                    Object this$relation = this.getRelation();
                    Object other$relation = other.getRelation();
                    if (this$relation == null) {
                        if (other$relation != null) {
                            return false;
                        }
                    } else if (!this$relation.equals(other$relation)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ElasticSearchVO.Total;
        }

        public int hashCode() {
            int result = 1;
            long $value = this.getValue();
            result = result * 59 + (int)($value >>> 32 ^ $value);
            Object $relation = this.getRelation();
            result = result * 59 + ($relation == null ? 43 : $relation.hashCode());
            return result;
        }

        public String toString() {
            return "ElasticSearchVO.Total(value=" + this.getValue() + ", relation=" + this.getRelation() + ")";
        }
    }

    public static class Hits<T> {
        private Total total;
        private float max_score;
        private List<HitsChild<T>> hits;

        public Hits() {
        }

        public Total getTotal() {
            return this.total;
        }

        public float getMax_score() {
            return this.max_score;
        }

        public List<HitsChild<T>> getHits() {
            return this.hits;
        }

        public void setTotal(final Total total) {
            this.total = total;
        }

        public void setMax_score(final float max_score) {
            this.max_score = max_score;
        }

        public void setHits(final List<HitsChild<T>> hits) {
            this.hits = hits;
        }

        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ElasticSearchVO.Hits)) {
                return false;
            } else {
                Hits<?> other = (Hits)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    label39: {
                        Object this$total = this.getTotal();
                        Object other$total = other.getTotal();
                        if (this$total == null) {
                            if (other$total == null) {
                                break label39;
                            }
                        } else if (this$total.equals(other$total)) {
                            break label39;
                        }

                        return false;
                    }

                    if (Float.compare(this.getMax_score(), other.getMax_score()) != 0) {
                        return false;
                    } else {
                        Object this$hits = this.getHits();
                        Object other$hits = other.getHits();
                        if (this$hits == null) {
                            if (other$hits != null) {
                                return false;
                            }
                        } else if (!this$hits.equals(other$hits)) {
                            return false;
                        }

                        return true;
                    }
                }
            }
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ElasticSearchVO.Hits;
        }

        public int hashCode() {
            int result = 1;
            Object $total = this.getTotal();
            result = result * 59 + ($total == null ? 43 : $total.hashCode());
            result = result * 59 + Float.floatToIntBits(this.getMax_score());
            Object $hits = this.getHits();
            result = result * 59 + ($hits == null ? 43 : $hits.hashCode());
            return result;
        }

        public String toString() {
            return "ElasticSearchVO.Hits(total=" + this.getTotal() + ", max_score=" + this.getMax_score() + ", hits=" + this.getHits() + ")";
        }
    }

    public class Shards {
        private long total;
        private long successful;
        private long skipped;
        private long failed;

        public Shards() {
        }

        public long getTotal() {
            return this.total;
        }

        public long getSuccessful() {
            return this.successful;
        }

        public long getSkipped() {
            return this.skipped;
        }

        public long getFailed() {
            return this.failed;
        }

        public void setTotal(final long total) {
            this.total = total;
        }

        public void setSuccessful(final long successful) {
            this.successful = successful;
        }

        public void setSkipped(final long skipped) {
            this.skipped = skipped;
        }

        public void setFailed(final long failed) {
            this.failed = failed;
        }

        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof ElasticSearchVO.Shards)) {
                return false;
            } else {
                ElasticSearchVO<?>.Shards other = (Shards)o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (this.getTotal() != other.getTotal()) {
                    return false;
                } else if (this.getSuccessful() != other.getSuccessful()) {
                    return false;
                } else if (this.getSkipped() != other.getSkipped()) {
                    return false;
                } else {
                    return this.getFailed() == other.getFailed();
                }
            }
        }

        protected boolean canEqual(final Object other) {
            return other instanceof ElasticSearchVO.Shards;
        }

        public int hashCode() {
            int resultx = 1;
            long $total = this.getTotal();
            int result = resultx * 59 + (int)($total >>> 32 ^ $total);
            long $successful = this.getSuccessful();
            result = result * 59 + (int)($successful >>> 32 ^ $successful);
            long $skipped = this.getSkipped();
            result = result * 59 + (int)($skipped >>> 32 ^ $skipped);
            long $failed = this.getFailed();
            result = result * 59 + (int)($failed >>> 32 ^ $failed);
            return result;
        }

        public String toString() {
            return "ElasticSearchVO.Shards(total=" + this.getTotal() + ", successful=" + this.getSuccessful() + ", skipped=" + this.getSkipped() + ", failed=" + this.getFailed() + ")";
        }
    }
}

