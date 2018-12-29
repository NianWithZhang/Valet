package niannian.valet.ClothesManageRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import niannian.valet.R;
import niannian.valet.ResponseModel.Clothes;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    List<Clothes> Clothes;

    public RecyclerViewAdapter(List<Clothes> Fruits){
        this.Clothes=Fruits;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item,parent,false);
        //将之前写好的list_view封装到一个View中
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        Clothes currentClothes=Clothes.get(position);
        //holder.imageView.setImageResource(currentClothes.image);
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
            textView=(TextView)itemView.findViewById(R.id.clothesName);
            imageView=(ImageView) itemView.findViewById(R.id.clothesImage);
        }
    }
}
