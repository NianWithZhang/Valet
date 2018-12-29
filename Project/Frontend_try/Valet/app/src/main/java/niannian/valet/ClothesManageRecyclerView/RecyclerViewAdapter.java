package niannian.valet.ClothesManageRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import niannian.valet.Data.ClothesImformation;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    List<ClothesImformation> Clothes;

    public RecyclerViewAdapter(List<ClothesImformation> Fruits){
        this.Clothes=Fruits;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view,parent,false);
        //将之前写好的list_view封装到一个View中
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Clothes currentClothes=Clothes.get(position);
        holder.imageView.setImageResource(currentClothes.image);
        holder.textView.setText(currentClothes.name);
    }



    @Override
    public int getItemCount() {
        return Clothes.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.textview);
            imageView=(ImageView) itemView.findViewById(R.id.imageview);
        }
    }
}

作者：穿梭侠
        链接：https://www.jianshu.com/p/c0eaf2c0f790
        來源：简书
        简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
