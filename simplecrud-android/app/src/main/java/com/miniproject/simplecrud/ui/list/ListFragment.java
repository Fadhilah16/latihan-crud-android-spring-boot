package com.miniproject.simplecrud.ui.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miniproject.simplecrud.adapter.ProductListAdapter;
import com.miniproject.simplecrud.databinding.FragmentListBinding;
import com.miniproject.simplecrud.ui.edit.EditFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.miniproject.simplecrud.models.ProductModel;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        container.removeAllViews();
        View root = binding.getRoot();
        RecyclerView productListView = binding.productsList;
        productListView.setHasFixedSize(true);
        ListFragment listFragment = new ListFragment();
        EditFragment editFragment = new EditFragment();
        ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(listFragment.getClass().getName());

        List<ProductModel> productsList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = "http://192.168.0.107:8080/api/products/";


            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try{

                        JSONArray productArray = new JSONArray(response);

                        for(int i=0; i<productArray.length();i++){
                            JSONObject productObject = productArray.getJSONObject(i);
                            Long id = new Long(productObject.getString("id"));
                            String name = productObject.getString("name");
                            String desc = productObject.getString("description");
                            BigDecimal price = new BigDecimal(productObject.getString("price")) ;
                            ProductModel productTemp = new ProductModel(id,name,desc,price );
                            productsList.add(productTemp);


                        }
//                        showDialog("Data", productArray.toString());

                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), productsList);
                        productListView.setAdapter(productListAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        productListView.setLayoutManager(layoutManager);


                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showDialog("Error", error.toString());


            }});
            requestQueue.add(stringRequest);




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showDialog(String title, String msg){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
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

}