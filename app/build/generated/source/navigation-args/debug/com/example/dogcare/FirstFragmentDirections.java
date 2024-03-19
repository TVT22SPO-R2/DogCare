package com.example.dogcare;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;

public class FirstFragmentDirections {
  private FirstFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionFirstFragmentToSecondFragment() {
    return new ActionOnlyNavDirections(R.id.action_FirstFragment_to_SecondFragment);
  }

  @NonNull
  public static NavDirections actionFirstFragmentToSettingsFragment() {
    return new ActionOnlyNavDirections(R.id.action_FirstFragment_to_SettingsFragment);
  }
}
