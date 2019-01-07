package niannian.valet.RecyclerViewAdapter;

import android.support.annotation.NonNull;
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
import niannian.valet.ResponseModel.WardrobeResponse;
import niannian.valet.ResponseModel.WardrobeResponseList;

@SuppressWarnings("ConstantConditions")
public class WardrobeRecyclerViewAdapter extends RecyclerView.Adapter<WardrobeRecyclerViewAdapter.MyHolder> {
    private final WardrobeResponseList Wardrobes;
    private final HashMap<Integer,Boolean> Maps= new HashMap<>();
    private final HashMap<Integer,Boolean>AllMaps= new HashMap<>();
    private wardrobeRecyclerViewOnItemClickListener onItemClickListener;

    public WardrobeRecyclerViewAdapter(WardrobeResponseList WardrobeList){

        this.Wardrobes=WardrobeList;
        for (int i = 0; i < Wardrobes.wardrobes.length; i++) {
            Maps.put(i, false);
        }
    }

    //获取最终的map存储数据
    public Map<Integer,Boolean> getMap(){
        return Maps;
    }

    //点击item选中CheckBox
    @SuppressWarnings("ConstantConditions")
    public void setSelectItem(int position) {
        //对当前状态取反
        //noinspection ConstantConditions
        if (Maps.get(position)) {
            Maps.put(position, false);
        } else {
            Maps.put(position, true);
        }
        notifyItemChanged(position);
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wardrobe_item,parent,false);
        //将之前写好的list_view封装到一个View中
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        WardrobeResponse currentWardrobes=Wardrobes.wardrobes[position];

        //holder.imageView.setImageResource(currentClothes.image);
        holder.textView.setText(currentWardrobes.name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Maps.put(position,isChecked);
            }
        });
        if(Maps.get(position)==null){
            Maps.put(position,false);
        }

        //noinspection ConstantConditions
        holder.checkBox.setChecked(Maps.get(position));


        //之后扩展使用
        AllMaps.put(position,true);

        holder.itemView.findViewById(R.id.wardrobeCard).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int pos = holder.getLayoutPosition();
                onItemClickListener.onItemClickListener(holder.itemView,pos);
            }
        });
    }



    @Override
    public int getItemCount() {

        return Wardrobes.wardrobes.length;
    }



    class MyHolder extends RecyclerView.ViewHolder{
        //ImageView imageView;
        final TextView textView;
        final CheckBox checkBox;
        private final wardrobeRecyclerViewOnItemClickListener listener;
        MyHolder(View itemView) {
            super(itemView);
            this.listener = onItemClickListener;
            textView= itemView.findViewById(R.id.wardrobeNameText);
            checkBox = itemView.findViewById(R.id.checkboxChooseWardrobe);
        }


    }
    public void setItemClickListener(wardrobeRecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface wardrobeRecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);
    }
}
