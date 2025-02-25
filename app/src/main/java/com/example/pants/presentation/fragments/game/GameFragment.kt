package com.example.pants.presentation.fragments.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.pants.R
import com.example.pants.databinding.FragmentGameBinding
import com.example.pants.presentation.utils.extension.collectFlow
import com.example.pants.presentation.utils.extension.setColoredText
import com.example.pants.presentation.utils.extension.showErrorDialog
import com.example.pants.presentation.utils.extension.showToast
import com.example.pants.presentation.fragments.SharedGameViewModel
import com.example.pants.presentation.utils.adapter.ColorListAdapter
import com.example.pants.presentation.fragments.picker.ColorPickerFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class GameFragment : Fragment() {

    private var _viewBinding: FragmentGameBinding? = null
    private val viewBinding get() = _viewBinding!!

    private val viewModel by activityViewModel<SharedGameViewModel>()

    private var _adapter: ColorListAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        _viewBinding = FragmentGameBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewBinding) {
            textTitle.setColoredText(getString(R.string.sort_the_pants))
            _adapter = ColorListAdapter { colorModel ->
                navigateToPicker(colorModel.name)
            }
            colorsList.adapter = adapter
            setRecyclerViewVisibility(false)
            collectFlow(viewModel.screenState) {
                if (it.colorBoard.isEmpty()) {
                    setRecyclerViewVisibility(false)
                } else {
                    setRecyclerViewVisibility(true)
                    adapter.submitList(it.colorBoard)
                }
            }
            collectFlow(viewModel.errorMessage, ::showErrorDialog)
            btnCheck.setOnClickListener {
                checkOrderAndChangeView()
            }
        }
    }

    private fun navigateToPicker(colorName: String) {
        val fragment = ColorPickerFragment().apply {
            arguments = Bundle().apply {
                putString(COLOR_NAME_ARG, colorName)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun checkOrderAndChangeView() {
        with(viewBinding) {
            val colors = viewModel.checkColorOrder(adapter.currentList)
            when {
                colors == null -> {
                    showToast(getString(R.string.success))
                }

                colors.isNotEmpty() -> {
                    adapter.apply {
                        setOriginalList()
                        submitList(colors)
                        setHelpMode(true)
                    }
                    textTitle.setColoredText(getString(R.string.pants_solution))
                    hseGradient.visibility = View.GONE
                    btnCheck.text = getString(R.string.try_again)
                    btnCheck.setOnClickListener {
                        resetNextButton()
                    }
                }
            }
        }
    }

    private fun setRecyclerViewVisibility(isVisible: Boolean) {
        with(viewBinding) {
            val visibility = if (isVisible) View.VISIBLE else View.GONE
            progressBar.visibility = if (isVisible) View.GONE else View.VISIBLE
            colorsList.visibility = visibility
            hseGradient.visibility = visibility
        }
    }


    private fun resetNextButton() {
        with(viewBinding) {
            hseGradient.visibility = View.VISIBLE
            textTitle.setColoredText(getString(R.string.sort_the_pants))
            adapter.apply {
                setHelpMode(false)
                resetToOriginalOrder()
            }
            btnCheck.text = getString(R.string.next)
            btnCheck.setOnClickListener {
                checkOrderAndChangeView()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
        _adapter = null
    }

    companion object {
        const val COLOR_NAME_ARG = "COLOR_NAME"
    }
}
