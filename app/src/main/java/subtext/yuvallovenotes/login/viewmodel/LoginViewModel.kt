package subtext.yuvallovenotes.login.viewmodel

import android.content.SharedPreferences
import android.util.Log.d
import android.util.Log.e
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.backendless.push.DeviceRegistrationResult
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.koin.java.KoinJavaComponent
import subtext.yuvallovenotes.R
import subtext.yuvallovenotes.YuvalLoveNotesApp
import subtext.yuvallovenotes.crossapplication.models.LoveLettersUser
import subtext.yuvallovenotes.crossapplication.models.UnVerifiedLoveLettersUser
import subtext.yuvallovenotes.crossapplication.network.NetworkCallback
import subtext.yuvallovenotes.crossapplication.utils.getDeviceDefaultCountryCode
import subtext.yuvallovenotes.crossapplication.utils.isPhoneNumberValid
import subtext.yuvallovenotes.login.network.LoginRepository
import subtext.yuvallovenotes.login.network.UserRegistrationCallback

class LoginViewModel : ViewModel() {


    companion object {
        private val TAG = LoginViewModel::class.simpleName
    }

    private val loginRepository: LoginRepository = KoinJavaComponent.get(LoginRepository::class.java)
    private val prefs: SharedPreferences = KoinJavaComponent.get(SharedPreferences::class.java)

    /**
     * Request a login, i.e, if the input data is valid, register or login the user and associate it with the given course name.
     * @param user An user details object that are not yet verified to be in the data base.
     * @param callback Callback to call when the process succeeds or fails
     */
    fun requestLogin(user: UnVerifiedLoveLettersUser, callback: UserRegistrationCallback) {

//        onSuccess.invoke() - Use when you want to quick-skip login process
        val userNetworkRegistrationRequestExecutor = UserNetworkRegistrationExecutor(user, callback)
        if (userInputValidation(user, callback)) {
            userNetworkRegistrationRequestExecutor.execute()
        }
    }

    /**
     * Returns true if the input data is valid
     * @param user An user details object.
     * @param callback Callback to call when an error occurred
     */
    private fun userInputValidation(user: LoveLettersUser, callback: UserRegistrationCallback): Boolean {
        d(TAG, "Validating user input")
        var isValid = false
        if (allRelevantFieldsHaveText(user, callback)) {
            d(TAG, "Fields are not blank")
            if (isPhoneNumberValid(user.phone, callback)) {
                isValid = true
            }
        }
        return isValid
    }

    /**
     * Helper class for the user login process.
     *
     * The login is made out of two parallel steps.
     * 1. Register the user
     * 2. Register the device to the push notifications service
     */
    private inner class UserNetworkRegistrationExecutor(val user: UnVerifiedLoveLettersUser, callback: UserRegistrationCallback) {

        /**
         * Starts the registration/login process
         */
        fun execute() {
            prefs.edit().putBoolean(YuvalLoveNotesApp.context.getString(R.string.pref_key_is_login_process_completed), false).apply()
            loginRepository.registerUser(user, registerUserCallback)
            val pushNotificationChannels = listOf("default", user.userName)
            loginRepository.registerToPushNotificationsService(pushNotificationChannels, registerNotificationsCallback)
        }

        private val registerUserCallback = object : NetworkCallback<LoveLettersUser> {

            override fun onSuccess(response: LoveLettersUser) {
                d(TAG, "User registration successful")
                val context = YuvalLoveNotesApp.context
                saveDataToPrefs(response)
                val isDeviceRegisteredToNotifications = prefs.getBoolean(context.getString(R.string.pref_key_device_registered_to_push_notifications), false)
                if (isDeviceRegisteredToNotifications) {
                    d(TAG, "User registration callback: Successful user and device registration on server")
                    prefs.edit().putBoolean(context.getString(R.string.pref_key_is_login_process_completed), true).apply()
                    callback.onSuccess()
                } else {
                    d(TAG, "User registration callback: Waiting for device registration to notifications")
                }
            }

            override fun onFailure(message: String) {
                e(TAG, "User registration failure: $message")
                callback.onError(message)
            }
        }

        private val registerNotificationsCallback = object : NetworkCallback<DeviceRegistrationResult> {

            override fun onSuccess(response: DeviceRegistrationResult) {
                d(TAG, "Device registered to notifications")
                val context = YuvalLoveNotesApp.context
                prefs.edit().putBoolean(context.getString(R.string.pref_key_device_registered_to_push_notifications), false).apply()
                val userName = prefs.getString(context.getString(R.string.pref_key_user_name), "")
                if (!userName.isNullOrBlank()) {
                    d(TAG, "Notifications registration callback: Successful device and user registration on server")
                    prefs.edit().putBoolean(context.getString(R.string.pref_key_is_login_process_completed), true).apply()
                    callback.onSuccess()
                } else {
                    d(TAG, "Notifications registration callback: Waiting for user registration")
                }
            }

            override fun onFailure(message: String) {
                e(TAG, "Device registration failure: $message")
                callback.onError(message)
            }
        }

    }

    private fun isPhoneNumberValid(phone: LoveLettersUser.Phone, callback: UserRegistrationCallback): Boolean {
        val defaultError = YuvalLoveNotesApp.context.getString(R.string.error_invalid_lover_number_inserted)
        if (PhoneNumberUtil.getInstance().isPhoneNumberValid(phone.regionNumber, phone.localNumber)) {
            return true
        }
        callback.onError(defaultError)
        return false
    }

    private fun allRelevantFieldsHaveText(user: LoveLettersUser, callback: UserRegistrationCallback): Boolean {
        val context = YuvalLoveNotesApp.context

        if (user.phone.isBlank()) {
            callback.onError(context.getString(R.string.error_invalid_lover_number_inserted))
            return false
        }

        return true
    }

    fun getUserName(): String {
        return prefs.getString(YuvalLoveNotesApp.context.getString(R.string.pref_key_user_name), "")!!
    }

    fun getLoverNickName(): String {
        return prefs.getString(YuvalLoveNotesApp.context.getString(R.string.pref_key_lover_name), "")!!
    }

    fun requestUserPhoneNumber(onCompletion: (regionNumber: String, localNumber: String) -> Unit) {
        val context = YuvalLoveNotesApp.context
        val defaultRegion = PhoneNumberUtil.getInstance().getDeviceDefaultCountryCode()
        val region = prefs.getString(context.getString(R.string.pref_key_phone_region_number).takeUnless { it.isBlank() }, defaultRegion)!!
        val local = prefs.getString(context.getString(R.string.pref_key_local_phone_number), "")!!
        onCompletion.invoke(region, local)
    }


    private fun saveDataToPrefs(user: LoveLettersUser) {
        d(TAG, "Saving data to shared preferences")
        val context = YuvalLoveNotesApp.context
        prefs.edit {
            putString(context.getString(R.string.pref_key_phone_region_number), user.phone.regionNumber)
            putString(context.getString(R.string.pref_key_local_phone_number), user.phone.localNumber)
            putString(context.getString(R.string.pref_key_full_target_phone_number), user.phone.fullNumber)
        }
    }

}