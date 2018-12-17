package com.king.androidcomponent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>> {

    private final String TAG = getClass().getSimpleName();
    private final LayoutInflater mInflater;

    private List<T> mDatas = new ArrayList<>();
    private OnItemClickListener<T> mOnItemClickLitener;
    private OnItemLongClickListener<T> mOnItemLongClickLitener;
    private Context mContext;
    private List<ViewBundle> mViewBundles = new ArrayList<>();
    ;

    public BaseAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        onBindVHLayoutId(mViewBundles);
    }

    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mViewBundles != null && mViewBundles.size() > 0) {
            ViewBundle viewBundle = mViewBundles.get(i);
            int layoutId = viewBundle.layoutId;
            View view = mInflater.inflate(layoutId, viewGroup, false);
            return (BaseViewHolder<T>) InstanceUtil.getInstance(viewBundle.VHclazz, new Class[]{View.class},new Object[]{view});

        } else {
            throw new IllegalArgumentException("mviewbundles can not be null or empty!");
        }
    }

    /**
     * 绑定 vh和layoutid
     * 该list的index表示对应的view type
     */
    protected abstract void onBindVHLayoutId(List<ViewBundle> viewBundle);


    @Override
    public void onBindViewHolder(@NonNull final BaseViewHolder<T> holder, int position) {
        T t = mDatas.get(position);
        try {
            holder.bindView(t, position, mContext);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        View itemView = holder.itemView;
        int holderLayoutPosition = holder.getLayoutPosition();
        if (mOnItemClickLitener != null) {
            itemView.setOnClickListener(
                    view -> mOnItemClickLitener.onItemClick(t, itemView, holderLayoutPosition));
        }
        if (mOnItemLongClickLitener != null) {
            itemView.setOnLongClickListener(
                    view -> mOnItemLongClickLitener.onItemLongClick(t, itemView, holderLayoutPosition));
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickLitener(OnItemClickListener<T> onItemClickLitener) {
        this.mOnItemClickLitener = onItemClickLitener;
    }

    /**
     * 如果viewbundles size=1 不需要重写该函数
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public void setOnItemLongClickLitener(OnItemLongClickListener<T> mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T t, View view, int position);

    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(T t, View view, int position);
    }

    public static abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public Context getContext() {
            return itemView.getContext();
        }

        protected abstract void bindView(T data, int position, Context context) throws ParseException;

    }

    public static class ViewBundle {
        public ViewBundle(int layoutId, Class<? extends BaseViewHolder> clazz) {
            this.layoutId = layoutId;
            this.VHclazz = clazz;
        }

        public int layoutId;
        public Class VHclazz;
    }


    public void addList(List<T> list) {
        if (list == null) return;
        mDatas.addAll(list);
        notifyItemRangeInserted(mDatas.size() - list.size(), mDatas.size() - 1);
    }
    public void removeItemAt(int position) {
        if (position<0||position>=mDatas.size())return;
        mDatas.remove(position);
        notifyItemMoved(position, position + 1);
    }
    public List<T> getList() {
        return mDatas;
    }

    public void addItem(T item) {
        mDatas.add(item);
        notifyItemInserted(mDatas.size() - 1);
    }

    public void setData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyItemRangeChanged(0,mDatas.size() - 1);
    }

    public void insertItem(T item, int position) {
        if (position > mDatas.size() - 1) {
            position = mDatas.size() - 1;
        }
        mDatas.add(position, item);
        notifyItemInserted(position);
    }
    public void addItemAtBegin(T item) {
        mDatas.add(0,item);
        notifyItemInserted(0);
    }

    public void replaceDate(List<T> list) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mDatas, list), true);
        diffResult.dispatchUpdatesTo(this);
        if (mDatas.size() != 0) {
            mDatas.clear();
        }
        mDatas.addAll(list);
    }

    public void clearList() {
        mDatas.clear();
        notifyDataSetChanged();
    }
}
