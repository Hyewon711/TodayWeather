package com.seo.todayweather.ui.style

import android.net.Uri
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.seo.todayweather.base.BaseFragment
import com.seo.todayweather.databinding.FragmentStyleWriteBinding
import com.seo.todayweather.util.common.PrefManager
import com.seo.todayweather.util.extension.changeFragment
import com.seo.todayweather.ui.viewmodel.StyleViewModel

class StyleWriteFragment :
    BaseFragment<FragmentStyleWriteBinding>(FragmentStyleWriteBinding::inflate) {
    private val viewModel: StyleViewModel by activityViewModels()
    private lateinit var getContent: ActivityResultLauncher<String>
    private var selectedImageUri: Uri? = null
    override fun onCreateView() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
    override fun onViewCreated() {
        initView()
    }

    private fun initView() {
        with(binding) {
            /* StyleFragment로 이동 */
            ivBack.setOnAvoidDuplicateClick {
                styleWriteFragment.changeFragment(this@StyleWriteFragment, StyleFragment())
            }
            getContent =
                registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                    uri?.let {
                        selectedImageUri = it
                        Glide.with(this@StyleWriteFragment).load(selectedImageUri).into(ivSelectImage)
                    }
                }
            ivSelectImage.setOnAvoidDuplicateClick {
                getContent.launch("image/*")
            }
            ivWrite.setOnAvoidDuplicateClick {
                if (inputTitleBody.text != null && inputContentsBody.text != null && selectedImageUri != null) {
                    val id = PrefManager.getInstance().getUserId
                    val userStyle = PrefManager.getInstance().selectStyle
                    val name = PrefManager.getInstance().getUserName
                    val title = inputTitleBody.text.toString()
                    val contents = inputContentsBody.text.toString()
                    val userUri = PrefManager.getInstance().getUserImage
                    val imageFileName = "${System.currentTimeMillis()}_${id}.jpg"
                    val imageFileRef =
                        FirestoreManager().storage.reference.child("images/$imageFileName")

                    imageFileRef.putFile(selectedImageUri!!)
                        .addOnSuccessListener {
                            imageFileRef.downloadUrl.addOnSuccessListener { uri ->
                                uploadImageAndSavePost(
                                    id,
                                    userStyle,
                                    name!!,
                                    title,
                                    contents,
                                    uri.toString(),
                                    userUri!!
                                )
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "등록에 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "게시글을 모두 입력해주세요.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun uploadImageAndSavePost(
        id: Long,
        userStyle: Int,
        name: String,
        title: String,
        contents: String,
        uri: String,
        userUri: String
    ) {
        viewModel.writeStylePost(id, userStyle, name, title, contents, uri, userUri)
        Toast.makeText(requireContext(), "게시글을 등록하였습니다.", Toast.LENGTH_SHORT).show()
        binding.styleWriteFragment.changeFragment(this@StyleWriteFragment, StyleFragment())
    }
}