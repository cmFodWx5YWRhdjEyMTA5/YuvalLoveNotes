package subtext.yuvallovenotes.registration.network

import android.util.Log
import com.backendless.push.DeviceRegistrationResult
import org.koin.java.KoinJavaComponent.get
import subtext.yuvallovenotes.R
import subtext.yuvallovenotes.YuvalLoveNotesApp
import subtext.yuvallovenotes.crossapplication.models.users.LoveLettersUser
import subtext.yuvallovenotes.crossapplication.models.users.UnRegisteredLoveLettersUser
import subtext.yuvallovenotes.crossapplication.network.BackendlessNetworkServiceImpl
import subtext.yuvallovenotes.crossapplication.network.UserRegistrationNetworkService
import subtext.yuvallovenotes.crossapplication.network.NetworkCallback
import subtext.yuvallovenotes.crossapplication.utils.LoveUtils

class RegistrationRepository {

    companion object {
        private val TAG: String = RegistrationRepository::class.simpleName!!
    }

    private val awarenessNetworkService: UserRegistrationNetworkService = get(BackendlessNetworkServiceImpl::class.java)

    private fun startOperationIfNetworkAvailable(operation: () -> Unit, networkCallback: NetworkCallback<*>?){
        val noNetworkError = YuvalLoveNotesApp.context.getString(R.string.error_no_network_connection)
        if (LoveUtils.isNetworkAvailable(YuvalLoveNotesApp.context)) {
            operation.invoke()
        } else {
            Log.w(TAG, "No network connection")
            networkCallback?.onFailure(noNetworkError)
        }
    }

    fun registerUser(user: UnRegisteredLoveLettersUser, callback: NetworkCallback<LoveLettersUser>) {
        startOperationIfNetworkAvailable({awarenessNetworkService.registerUser(user, callback)}, callback)
    }

    fun registerToPushNotificationsService(channels: List<String>?, callback: NetworkCallback<DeviceRegistrationResult>?){
        startOperationIfNetworkAvailable({awarenessNetworkService.registerDeviceToPushNotificationsService(channels, callback)}, callback)
    }

}
