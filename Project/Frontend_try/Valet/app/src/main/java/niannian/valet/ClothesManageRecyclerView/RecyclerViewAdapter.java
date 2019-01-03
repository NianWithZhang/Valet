package niannian.valet.ClothesManageRecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import niannian.valet.R;
import niannian.valet.ResponseModel.ClothesResponse;
import niannian.valet.ResponseModel.ClothesResponseList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    public ClothesResponseList clothes;

    private HashMap<Integer,Boolean> Maps=new HashMap<Integer,Boolean>();
    private HashMap<Integer,Boolean> AllMaps=new HashMap<Integer,Boolean>();
    public RecyclerViewOnItemClickListener onItemClickListener;

    private ArrayList<Integer> selectedIDList;

    public RecyclerViewAdapter(ClothesResponseList ClothesList, ArrayList<Integer> selectedIDList){

        this.selectedIDList = selectedIDList;
        this.clothes =ClothesList;
//        for (int i = 0; i < clothes.clothes.length; i++) {
//            Maps.put(i, false);
//        }
    }

//    //获取最终的map存储数据
//    public Map<Integer,Boolean> getMap(){
//        return Maps;
//    }

    //后续扩展 - 获取最终的map存储数据
//   public Map<Integer,Boolean>getAllMap(){
//        return AllMaps;
//    }

//    //点击item选中CheckBox
//    public void setSelectItem(int position) {
//        //对当前状态取反
//        if (Maps.get(position)) {
//            Maps.put(position, false);
//        } else {
//            Maps.put(position, true);
//        }
//        notifyItemChanged(position);
//    }



    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.clothes_item,parent,false);
        //将之前写好的list_view封装到一个View中
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        if(selectedIDList.contains(clothes.clothes[position].id)){
            ((CheckBox)holder.itemView.findViewById(R.id.checkboxChooseClothes)).setChecked(true);
//            Maps.put(position,true);
        }

        ClothesResponse currentClothes= clothes.clothes[position];
        currentClothes.setImage(holder.itemView.getContext(),holder.clothesImage);
        //holder.imageView.setImageResource(currentClothes.image);
        holder.clothesNameText.setText(currentClothes.name);
        holder.clothesTypeText.setText(currentClothes.getTypeName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Maps.put(position,isChecked);
                Integer temp = clothes.clothes[position].id;
                if(selectedIDList.contains(temp))
                    selectedIDList.remove(temp);
                else
                    selectedIDList.add(temp);
            }
        });
//        if(Maps.get(position)==null){
//            Maps.put(position,false);
//        }

//        holder.checkBox.setChecked(Maps.get(position));

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

        return clothes.clothes.length;
    }



    class MyHolder extends RecyclerView.ViewHolder{
        //ImageView imageView;
        TextView clothesNameText,clothesTypeText;
        ImageView clothesImage;
        CheckBox checkBox;
        private RecyclerViewOnItemClickListener listener;
        public MyHolder(View itemView) {
            super(itemView);
            this.listener = onItemClickListener;
            clothesNameText =(TextView)itemView.findViewById(R.id.clothesNameText);
            clothesTypeText =(TextView)itemView.findViewById(R.id.clothesType);
            clothesImage = (ImageView)itemView.findViewById(R.id.clothesImage);
            //imageView=(ImageView) itemView.findViewById(R.id.clothesImage);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkboxChooseClothes);
        }



    }
    public void setItemClickListener(RecyclerViewOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public interface RecyclerViewOnItemClickListener {
        //点击事件
        void onItemClickListener(View view, int position);
    }
}
