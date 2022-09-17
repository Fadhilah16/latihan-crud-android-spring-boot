package com.miniproject.simplecrud.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miniproject.simplecrud.R;
import com.miniproject.simplecrud.ui.edit.EditFragment;

import java.util.ArrayList;
import java.util.List;

import com.miniproject.simplecrud.models.ProductModel;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    private List<ProductModel> listProduct = new ArrayList<>();
    private Context context;

    public ProductListAdapter(Context context, List<ProductModel> listProduct) {
        this.context = context;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);

        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.product_item;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = listProduct.get(position);
        holder.nameValue.setText(product.getName());
        holder.descValue.setText(product.getDescription());
        holder.priceValue.setText(product.getPrice().toString());
        holder.editButton.setTag(product.getId().toString());
        holder.deleteButton.setTag(product.getId().toString());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle dataId = new Bundle();
                dataId.putString("id",product.getId().toString());
                EditFragment editFragment = new EditFragment();
                editFragment.setArguments(dataId);
                ListFragment listFragment = new ListFragment();


                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.layout_list, editFragment).commit();

            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(product.getId().toString());


            }
        });

    }

    private void showDialog(String title, String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();


        alertDialog.show();
    }

    private void showDeleteDialog(String id){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        dialog.setTitle("Delete");
        dialog.setMessage("Are you sure to delete this product?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String URL = "http://192.168.0.107:8080/api/products/"+id;
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
//                            ListFragment listFragment = new ListFragment();
//                            Fragment currentFragment = ((FragmentActivity) context).getSupportFragmentManager().findFragmentById(R.id.navigation_list);
//                            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().detach(currentFragment).attach(currentFragment).commitNow();
                            NavController navController = Navigation.findNavController((FragmentActivity) context, R.id.nav_host_fragment_activity_main);
                            navController.navigate(R.id.navigation_list);
                            showDialog("Success", "product has been deleted");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showDialog("Error", error.toString());


                    }});
                requestQueue.add(stringRequest);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = dialog.create();


        alertDialog.show();

    }

    @Override
    public int getItemCount() {
        return (listProduct != null) ? listProduct.size() : 0 ;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        public TextView nameValue;
        public TextView descValue;
        public TextView priceValue;
        public ImageButton editButton;
        public ImageButton deleteButton;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameValue = itemView.findViewById(R.id.name_value);
            descValue = itemView.findViewById(R.id.desc_value);
            priceValue = itemView.findViewById(R.id.price_value);
            editButton = itemView.findViewById(R.id.edit_btn);
            deleteButton = itemView.findViewById(R.id.delete_btn);


        }
    }
}
