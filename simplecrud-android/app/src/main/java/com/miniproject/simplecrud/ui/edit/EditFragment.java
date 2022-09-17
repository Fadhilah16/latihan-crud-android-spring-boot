package com.miniproject.simplecrud.ui.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miniproject.simplecrud.R;
import com.miniproject.simplecrud.databinding.FragmentEditBinding;
import com.miniproject.simplecrud.ui.exit.ExitViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.miniproject.simplecrud.models.ProductModel;

public class EditFragment extends Fragment {

    private FragmentEditBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ExitViewModel exitViewModel =
                new ViewModelProvider(this).get(ExitViewModel.class);

        binding = FragmentEditBinding.inflate(inflater, container, false);
        container.removeAllViews();
        View root = binding.getRoot();
        List<ProductModel> productsList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        Bundle args= getArguments();
        String URL = "http://192.168.0.107:8080/api/products/"+args.getString("id");
        EditText nameValue = binding.nameField;
        EditText descValue = binding.descField;
        EditText priceValue = binding.priceField;
        Button updateButton = binding.btnUpdate;
        ImageButton backButton = binding.backButton;
        ListFragment listFragment = new ListFragment();
        EditFragment editFragment = new EditFragment();
        ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().addToBackStack(editFragment.getClass().getName());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try{
                    JSONObject product = new JSONObject(response);
                    nameValue.setText(product.getString("name"));
                    descValue.setText(product.getString("description"));
                    priceValue.setText(product.getString("price"));



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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameValue.getText().toString();
                String desc = descValue.getText().toString();
                String price =priceValue.getText().toString();

                String URL_UPDATE = "http://192.168.0.107:8080/api/products/";

                try{
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", args.getString("id"));
                    jsonBody.put("name", name);
                    jsonBody.put("description", desc);
                    jsonBody.put("price", price);
                    final String requestBody = jsonBody.toString();



                    StringRequest stringRequestPut = new StringRequest(Request.Method.PUT, URL_UPDATE, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.layout_edit, new com.miniproject.simplecrud.ui.list.ListFragment()).commitNow();


                            showDialog("Success", "product successfully updated");


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject data = new JSONObject(responseBody);
                                JSONArray msgs = data.getJSONArray("messages");
                                String messages = "";
                                for(int i=0;i<msgs.length();i++){
                                    String msg = msgs.getString(i);
                                    messages += " > "+ msg.toString()+"\n";
                                }
                                showDialog("Error", messages);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }catch (UnsupportedEncodingException e){
                                e.printStackTrace();
                            }
                        }


                    }) {

                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            String responseString = "";
                            if (response != null) {
                                responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                            }
                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                        }
                    };
                    requestQueue.add(stringRequestPut);


                }catch (JSONException e){
                    e.printStackTrace();
                }



            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.layout_edit, new com.miniproject.simplecrud.ui.list.ListFragment()).commitNow();

            }
        });


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