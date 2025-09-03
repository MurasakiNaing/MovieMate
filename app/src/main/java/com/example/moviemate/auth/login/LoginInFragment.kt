package com.example.moviemate.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.moviemate.R
import com.example.moviemate.activities.MainActivity
import com.example.moviemate.databinding.FragmentLoginInBinding
import com.example.moviemate.shared.firestore.FirebaseAuthService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginInFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginInBinding
    private lateinit var navController: NavController

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var emailInputText: TextInputEditText
    private lateinit var passwordInputLayout : TextInputLayout
    private lateinit var passwordInputText : TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController(view)
        binding.btnNavigateSignUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)

        emailInputLayout = binding.emailField
        emailInputText = binding.emailInput

        passwordInputLayout = binding.passwordField
        passwordInputText = binding.passwordInput

        setUpLoading()
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.btnNavigateSignUp -> navController.navigate(R.id.action_loginInFragment_to_signUpFragment)
            binding.btnLogin -> validate()
        }
    }

    private fun validate() {
        var isValid = true
        if (emailInputText.text.isNullOrBlank()) {
            emailInputLayout.error = "Email cannot be empty"
            isValid = false
        } else {
            emailInputLayout.error = null
        }
        if (passwordInputText.text.isNullOrBlank()) {
            passwordInputLayout.error = "Please enter password"
            isValid = false
        } else {
            passwordInputLayout.error = null
        }

        if (isValid) {
            signIn()
        }
    }

    private fun signIn() {
        binding.loadingOverlay.visibility = View.VISIBLE
        FirebaseAuthService.signIn(emailInputText.text.toString(), passwordInputText.text.toString(), requireActivity()) { success ->
            if (success) {
                Toast.makeText(activity, "Sign in successful", Toast.LENGTH_SHORT).show()
                redirectToHome()
            } else {
                binding.loadingOverlay.visibility = View.GONE
                Toast.makeText(activity, "Incorrect email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpLoading() {
        if (FirebaseAuthService.isUserLoggedIn()) {
            binding.loadingOverlay.visibility = View.VISIBLE
            redirectToHome()
        }
    }

    private fun redirectToHome() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}