package com.example.moviemate.auth.signin

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import com.example.moviemate.activities.MainActivity
import com.example.moviemate.databinding.FragmentSignUpBinding
import com.example.moviemate.shared.firestore.FireStoreService
import com.example.moviemate.shared.firestore.FirebaseAuthService
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpFragment : Fragment(), View.OnClickListener {

    val strongRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#&?])[A-Za-z\\d@$!%*#&?]{8,}$")
    val normalRegex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*#&?]{6,}$")
    val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var pwStrengthIndicator: LinearProgressIndicator

    private lateinit var emailTextInputLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var pwTextInputLayout: TextInputLayout
    private lateinit var pwInput : TextInputEditText

    private lateinit var confirmPwInputLayout: TextInputLayout
    private lateinit var confirmPwInput : TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAlreadyHaveAcc.setOnClickListener(this)
        pwStrengthIndicator = binding.pwStrengthIndicator
        emailTextInputLayout = binding.emailField
        emailInput = binding.emailInput
        pwTextInputLayout = binding.passwordField
        pwInput = binding.passwordInput
        confirmPwInputLayout = binding.confirmPasswordField
        confirmPwInput = binding.confirmPwInput
        setUpPasswordTextWatcher()
        binding.buttonSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.tvAlreadyHaveAcc -> activity?.onBackPressedDispatcher?.onBackPressed()
            binding.buttonSignUp -> validate()
        }
    }

    private fun setUpPasswordTextWatcher() {

        pwInput.addTextChangedListener(object : TextWatcher {
            private var currentColor = Color.RED

            override fun afterTextChanged(s: Editable?) {
                val pw = s.toString()
                val strength = checkPasswordStrength(pw)

                val (progress, newColor) = when(strength) {
                    "Weak" -> Pair(33, Color.RED)
                    "Normal" -> Pair(66, "#FFC107".toColorInt())
                    "Strong" -> Pair(100, "#4CAF50".toColorInt())
                    else -> Pair(0, Color.RED)
                }

                animateProgress(progress)
                animateColor(currentColor, newColor)
                currentColor = newColor
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.pwStrengthIndicator.visibility = View.VISIBLE
            }
        })

    }

    private fun checkPasswordStrength(password: String) : String {
        return when {
            password.matches(strongRegex) -> "Strong"
            password.matches(normalRegex) -> "Normal"
            else -> "Weak"
        }
    }

    private fun animateProgress(newProgress: Int) {
        ObjectAnimator.ofInt(pwStrengthIndicator, "Progress", pwStrengthIndicator.progress, newProgress).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            start()
        }
    }

    private fun animateColor(startColor: Int, endColor: Int) {
         ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor).apply {
             duration = 500
             addUpdateListener {
                 pwStrengthIndicator.setIndicatorColor(it.animatedValue as Int)
             }
             start()
        }
    }

    private fun validate() {
        var isValid = true
        if(!emailInput.text!!.matches(emailRegex)) {
            emailTextInputLayout.error = "Enter valid email address"
            isValid = false
        } else {
            emailTextInputLayout.error = null
        }

        if (pwInput.text.isNullOrBlank()) {
            pwTextInputLayout.error = "Please enter password"
            isValid = false
        } else {
            pwTextInputLayout.error = null
        }

        if (confirmPwInput.text.isNullOrBlank()) {
            confirmPwInputLayout.error = "Please enter confirm password"
            return
        } else {
            confirmPwInputLayout.error = null
        }

        if (confirmPwInput.text.toString() != pwInput.text.toString()) {
            confirmPwInputLayout.error = "Passwords does not match"
            isValid = false
        } else {
            confirmPwInputLayout.error = null
        }

        if (isValid) {
            signUp()
        }
    }

    private fun signUp() {
        FirebaseAuthService.signUp(emailInput.text.toString(), pwInput.text.toString(), requireActivity()) {success ->
            if (success) {
                initUserCollection()
            } else {
                Toast.makeText(activity, "Sign up failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initUserCollection() {
        FirebaseAuthService.getCurrentUser()?.let {user ->
            FireStoreService.createUserCollection(user.uid, user.email!!) {
                Toast.makeText(activity, "Sign up successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

}