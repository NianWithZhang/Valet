package niannian.valet.ClothesManageRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import niannian.valet.R;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.ResponseModel.ClothesResponseList;

public class WardrobeRecyclerViewAdapter extends RecyclerView.Adapter<WardrobeRecyclerViewAdapter.MyHolder> {
    ClothesResponseList Clothes;
    private HashMap<Integer,Boolean> Maps=new HashMap<Integer,Boolean>();
    private HashMap<Integer,Boolean>AllMaps=new HashMap<Integer,Boolean>();
    public RecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener;

    public WardrobeRecyclerViewAdapter(ClothesResponseList ClothesList){

        this.Clothes=ClothesList;
        for (int i = 0; i < Clothes.clothes.length; i++) {
            Maps.put(i, false);
        }
    }

    //获取最终的map存储数据
    public Map<Integer,Boolean> getMap(){
        return Maps;
    }

    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (Maps.get(position)) {
            Maps.put(position, false);
        } else {
            Maps.put(position, true);
        }
        notifyItemChanged(position);
    }



    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wardrobe_item,parent,false);
        //将之前写好的list_view封装到一个View中
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        ClothesResponse currentClothes=Clothes.clothes[position];

        //holder.imageView.setImageResource(currentClothes.image);
        holder.textView.setText(currentClothes.name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Maps.put(position,isChecked);
            }
        });
        if(Maps.get(position)==null){
            Maps.put(position,false);
        }

        holder.checkBox.setChecked(Maps.get(position));


        //之后扩展使用
        AllMaps.put(position,true);

        holder.itemView.findViewById(R.id.clothesCard).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClickListener(holder.itemView,pos);
            }
        });
    }



    @Override
    public int getItemCount() {

        return Clothes.clothes.length;
    }



    class MyHolder extends RecyclerView.ViewHolder{
        //ImageView imageView;
        TextView textView;
        CheckBox checkBox;
        private RecyclerViewAdapter.RecyclerViewOnItemClickListener listener;
        public MyHolder(View itemView) {
            super(itemView);
            this.listener = onItemClickListener;
            textView=(TextView)itemView.findViewById(R.id.clothesNameText);
            //imageView=(ImageView) itemView.findViewById(R.id.clothesImage);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkboxChooseClothes);
        }


    }
    public void setItemClickListener(RecyclerViewAdapter.RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);
    }
}
