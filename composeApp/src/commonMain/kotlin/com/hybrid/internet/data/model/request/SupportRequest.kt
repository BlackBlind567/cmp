import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportRequest(
    @SerialName("alt_mobile")
    val altMobile: String,

    @SerialName("alt_email")
    val altEmail: String,

    @SerialName("category")
    val category: String,

    @SerialName("sub_category")
    val subCategory: String,

    @SerialName("location")
    val location: String,

    @SerialName("remark")
    val remark: String,

    @SerialName("message")
    val message: String,

    // Image yahan Base64 string format mein jayegi
    @SerialName("image")
    val image: String? = null
)