// Generated by view binder compiler. Do not edit!
package com.example.dogcare.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.dogcare.R;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CustomDialogLayoutBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ListView multiChoiceItems;

  @NonNull
  public final TextInputEditText textInput;

  private CustomDialogLayoutBinding(@NonNull LinearLayout rootView,
      @NonNull ListView multiChoiceItems, @NonNull TextInputEditText textInput) {
    this.rootView = rootView;
    this.multiChoiceItems = multiChoiceItems;
    this.textInput = textInput;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CustomDialogLayoutBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CustomDialogLayoutBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.custom_dialog_layout, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CustomDialogLayoutBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.multiChoiceItems;
      ListView multiChoiceItems = ViewBindings.findChildViewById(rootView, id);
      if (multiChoiceItems == null) {
        break missingId;
      }

      id = R.id.textInput;
      TextInputEditText textInput = ViewBindings.findChildViewById(rootView, id);
      if (textInput == null) {
        break missingId;
      }

      return new CustomDialogLayoutBinding((LinearLayout) rootView, multiChoiceItems, textInput);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
