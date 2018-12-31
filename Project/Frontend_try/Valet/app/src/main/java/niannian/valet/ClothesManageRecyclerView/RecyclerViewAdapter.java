package niannian.valet.ClothesManageRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import niannian.valet.R;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.ResponseModel.ClothesResponseList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    ClothesResponseList Clothes;

    public RecyclerViewAdapter(ClothesResponseList ClothesList){
        this.Clothes=ClothesList;
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
        ClothesResponse currentClothes=Clothes.clothes[position];
        //holder.imageView.setImageResource(currentClothes.image);
        holder.textView.setText(currentClothes.name);
    }



    @Override
    public int getItemCount() {
        return Clothes.clothes.length;
    }
    class MyHolder extends RecyclerView.ViewHolder{
        //ImageView imageView;
        TextView textView;
        public MyHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.clothesNameText);
            //imageView=(ImageView) itemView.findViewById(R.id.clothesImage);
        }
    }
}
