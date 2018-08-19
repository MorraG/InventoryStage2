package com.example.user.inventorystage2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.inventorystage2.utils.Constants;
import com.example.user.inventorystage2.Data.InventoryContract;

public class AddProductFragment extends Fragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentProductUri;
    private EditText productName_et;
    private EditText price_et;
    private EditText quantity_et;
    private EditText suplName_et;
    private EditText suplPhone_et;

    private Button mButtonDash;
    private Button mButtonPlus;
    private Button save_product_btn;
    private ImageButton call_supl_btn;

    private int quantityAddProduct;

    public AddProductFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addproduct_inventory, container, false);
        setHasOptionsMenu(true);
        //Find views
        productName_et = rootView.findViewById(R.id.editProductName);
        price_et = rootView.findViewById(R.id.editPrice);
        quantity_et = rootView.findViewById(R.id.editQuantity);
        suplName_et = rootView.findViewById(R.id.editSupplierName);
        suplPhone_et = rootView.findViewById(R.id.editSupplierPhone);
        save_product_btn = rootView.findViewById(R.id.save_btn);
        mButtonDash = rootView.findViewById(R.id.meno);
        mButtonPlus = rootView.findViewById(R.id.piu);
        call_supl_btn = rootView.findViewById(R.id.supplier_phone_btn);

        //Set click listeners on buttons
        save_product_btn.setOnClickListener(this);


        Bundle bundle = getArguments();
        String uriString = null;

        if (bundle != null) uriString = bundle.getString(Constants.PRODUCT_URI);
        if (uriString != null) mCurrentProductUri = Uri.parse(uriString);

        //controllo quantità inserita con TextWatcher and Editable class.
        quantity_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                quantityAddProduct = 0;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    quantityAddProduct = 0;
                } else {
                    quantityAddProduct = Integer.parseInt(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    quantityAddProduct = 0;
                } else {
                    quantityAddProduct = Integer.parseInt(quantity_et.getText().toString());
                }

            }
        });

        //Set the title that corresponds to the fragment
        if (mCurrentProductUri == null) {

            getActivity().setTitle(getString(R.string.add_product));
            getActivity().invalidateOptionsMenu();
            call_supl_btn.setVisibility(View.GONE);

            mButtonDash.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (quantityAddProduct > 0) {

                        quantityAddProduct = quantityAddProduct - 1;
                        quantity_et.setText(String.valueOf(quantityAddProduct));

                    } else {

                        Toast.makeText(getActivity(), getString(R.string.quantity_cannot_be_negative), Toast.LENGTH_SHORT).show();

                    }

                }
            });

            mButtonPlus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    quantityAddProduct = quantityAddProduct + 1;
                    quantity_et.setText(String.valueOf(quantityAddProduct));

                }
            });
        } else {
            getActivity().setTitle(getString(R.string.edit_product));
            getLoaderManager().initLoader(Constants.SINGLE_PRODUCT_LOADER, null, this);
        }
        return rootView;
    }

    @Override
        public void onPrepareOptionsMenu(Menu menu) {
            super.onPrepareOptionsMenu(menu);
            if (mCurrentProductUri == null) {
                MenuItem menuItem = menu.findItem(R.id.action_delete);
                menuItem.setVisible(false);
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_with_delete, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                openAlertDialogForDelete();
            }
            return super.onOptionsItemSelected(item);
        }

        private void openAlertDialogForDelete() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog);
            builder.setMessage("Do you want to delete this item from the database?");
            builder.setPositiveButton("Yes, delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteProduct();
                    getActivity().onBackPressed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.create();
            builder.show();
        }

        private void deleteProduct() {
            if (mCurrentProductUri != null) {
                int rowsDeleted = getActivity().getContentResolver().delete(mCurrentProductUri, null, null);
                // Show a toast message depending on whether or not the delete was successful.
                if (rowsDeleted == 0) {
                    // If no rows were deleted, then there was an error with the delete.
                    Toast.makeText(getActivity(), R.string.Error_during_delete,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the delete was successful and we can display a toast.
                    Toast.makeText(getActivity(), R.string.Successful_deletingProd,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }


        @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new CursorLoader(getActivity(), mCurrentProductUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int productNameColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.PRICE);
            int supplierColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.ProductEntry.SUPPLIER_PHONE_NUMBER);

            String productName = cursor.getString(productNameColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            float price = cursor.getFloat(priceColumnIndex);
            String supplierName = cursor.getString(supplierColumnIndex);
            final String supplierPhone = cursor.getString(supplierPhoneColumnIndex);

            productName_et.setText(productName);
            price_et.setText(String.valueOf(price));
            quantity_et.setText(String.valueOf(quantity));
            suplName_et.setText(String.valueOf(supplierName));
            suplPhone_et.setText(String.valueOf(supplierPhone));

            quantityAddProduct = cursor.getInt(quantityColumnIndex);

            call_supl_btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);

                    intent.setData(Uri.parse("tel:" + supplierPhone));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

            mButtonDash.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (quantityAddProduct > 0) {

                        quantityAddProduct = quantityAddProduct - 1;
                        quantity_et.setText(String.valueOf(quantityAddProduct));

                    } else {

                        Toast.makeText(getActivity(), getString(R.string.quantity_cannot_be_negative), Toast.LENGTH_SHORT).show();

                    }

                }
            });

            mButtonPlus.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    quantityAddProduct = quantityAddProduct + 1;
                    quantity_et.setText(String.valueOf(quantityAddProduct));

                }
            });
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        if (saveProduct()) {
            ProductListFragment prodFrag = new ProductListFragment();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, prodFrag)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean saveProduct() {


        //Make sure that product name is not null
        String productName = productName_et.getText().toString().trim();
        if (TextUtils.isEmpty(productName)) {
            Toast.makeText(getActivity(), R.string.product_name_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Make sure that supl name is not null
        String supplierName = suplName_et.getText().toString().trim();
        if (TextUtils.isEmpty(supplierName)) {
            Toast.makeText(getActivity(), R.string.supplier_name_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Make sure that supl name is not null
        String supplierPhone = suplPhone_et.getText().toString().trim();
        if (TextUtils.isEmpty(supplierPhone)) {
            Toast.makeText(getActivity(), R.string.supplier_phone_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Make sure quantity is a positive integer

        int quantityInStock;

        try {

            quantityInStock = Integer.valueOf(quantity_et.getText().toString().trim());
            if (quantityInStock < 1) {
                Toast.makeText(getActivity(), R.string.quantity_should_be_positive, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(getActivity(), R.string.quantity_should_be_positive, Toast.LENGTH_SHORT).show();
            return false;
        }
        //Make sure sale price is positive number
        float salePrice;
        try {
            salePrice = Float.valueOf(price_et.getText().toString().trim());
            if (salePrice < 0) {
                Toast.makeText(getActivity(), R.string.price_should_be_number, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException nfe) {
            Toast.makeText(getActivity(), R.string.price_should_be_number, Toast.LENGTH_SHORT).show();
            return false;
        }


        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.PRODUCT_NAME, productName);
        values.put(InventoryContract.ProductEntry.PRICE, salePrice);
        values.put(InventoryContract.ProductEntry.QUANTITY, quantityInStock);
        values.put(InventoryContract.ProductEntry.SUPPLIER_NAME, supplierName);
        values.put(InventoryContract.ProductEntry.SUPPLIER_PHONE_NUMBER, supplierPhone);

        if (mCurrentProductUri == null) {
            //This is new product entry
            Uri newUri = getActivity().getContentResolver().insert(InventoryContract.ProductEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(getActivity(), R.string.error_saving_product, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(getActivity(), getString(R.string.product_saved_successfully), Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            // Otherwise this is an existing product, so update the product
            int rowsAffected = getActivity().getContentResolver().update(mCurrentProductUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(getActivity(), R.string.error_saving_product, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(getActivity(), getString(R.string.product_saved_successfully), Toast.LENGTH_SHORT).show();
                return true;
            }
        }
    }
}
