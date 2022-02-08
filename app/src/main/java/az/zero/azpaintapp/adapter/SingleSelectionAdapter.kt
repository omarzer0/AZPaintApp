package az.zero.azpaintapp.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import az.zero.azpaintapp.R
import az.zero.azpaintapp.data.models.ActionItem
import az.zero.azpaintapp.databinding.ItemActionBinding

class SingleSelectionAdapter(
    val onActionClick: (ActionItem) -> Unit,
) : ListAdapter<ActionItem, SingleSelectionAdapter.ItemViewHolder>(COMPARATOR) {

    private var lastCheckedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) holder.bind(currentItem)
    }

    inner class ItemViewHolder(private val binding: ItemActionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // add clickListener in init block to limit the number of clickListeners being instantiated
        init {
            binding.apply {
                root.setOnClickListener {
                    onActionClick(getItem(adapterPosition))
                    onSingleItemUnSelected(binding)
                    notifyItemChanged(lastCheckedPosition)
                    lastCheckedPosition = adapterPosition
                }
            }
        }

        fun bind(currentItem: ActionItem) {
            binding.apply {
                ivActionImage.setImageResource(currentItem.image)
                if (adapterPosition != lastCheckedPosition) {
                    onSingleItemSelected(binding)
                } else {
                    onSingleItemUnSelected(binding)
                }
            }
        }
    }

    private fun onSingleItemSelected(binding: ItemActionBinding) = binding.apply {
        binding.cvContainer.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setCardBackgroundColor(context.getColor(R.color.unselected_color))
            }
        }
    }

    private fun onSingleItemUnSelected(binding: ItemActionBinding) = binding.apply {
        binding.cvContainer.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setCardBackgroundColor(context.getColor(R.color.selected_color))
            }
        }
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<ActionItem>() {
            override fun areItemsTheSame(oldItem: ActionItem, newItem: ActionItem): Boolean =
                oldItem.action == newItem.action


            override fun areContentsTheSame(oldItem: ActionItem, newItem: ActionItem): Boolean =
                oldItem == newItem
        }
    }
}