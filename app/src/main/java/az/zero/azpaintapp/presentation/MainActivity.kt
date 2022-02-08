package az.zero.azpaintapp.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import az.zero.azpaintapp.adapter.SingleSelectionAdapter
import az.zero.azpaintapp.databinding.ActivityMainBinding
import az.zero.azpaintapp.databinding.ColorsCardBinding


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val actionAdapter = SingleSelectionAdapter {
        viewModel.preformAction(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setUpRv()
        observeState()
        handleClicksOnColorsCard(binding.colorCard)
    }


    private fun observeState() {
        viewModel.state.observe(this) { mainState ->
            actionAdapter.submitList(mainState.actionList)
            binding.paintSpace.apply {
                setPaintColor(mainState.selectedColor)
                changeViewType(mainState.typeOfView)
                setPaintColor(mainState.selectedColor)
            }
            binding.colorCard.colorsCardRoot.isVisible = mainState.paletteIsVisible
        }
    }

    private fun setUpRv() {
        binding.rvActions.apply {
            adapter = actionAdapter
            itemAnimator = null
        }
    }


    private fun handleClicksOnColorsCard(colorCard: ColorsCardBinding) {
        colorCard.cvBlack.setOnClickListener { viewModel.colorChanged(Color.BLACK) }
        colorCard.cvBlue.setOnClickListener { viewModel.colorChanged(Color.BLUE) }
        colorCard.cvGreen.setOnClickListener { viewModel.colorChanged(Color.GREEN) }
        colorCard.cvRed.setOnClickListener { viewModel.colorChanged(Color.RED) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}