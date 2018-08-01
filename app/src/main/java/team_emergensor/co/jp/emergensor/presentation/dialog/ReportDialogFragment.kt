package team_emergensor.co.jp.emergensor.presentation.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import team_emergensor.co.jp.emergensor.R
import team_emergensor.co.jp.emergensor.databinding.DialogReportBinding
import team_emergensor.co.jp.emergensor.domain.entity.DangerousType
import team_emergensor.co.jp.emergensor.presentation.home.HomeActivity


class ReportDialogFragment : DialogFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(activity as HomeActivity).get(ReportDialogViewModel::class.java)
    }

    private lateinit var binding: DialogReportBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity

        binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_report, null, false)
        binding.viewModel = viewModel

        val builder = AlertDialog.Builder(getActivity())
        builder.setView(binding.root)
                .setNegativeButton("cancel", { dialog, which -> })
                .setPositiveButton("send", { dialog, which ->
                })

        binding.radioGroup.also {
            it.check(R.id.radioButtonViolence)
            it.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioButtonViolence -> viewModel.type = DangerousType.VIOLENT
                    R.id.radioButtonDisaster -> viewModel.type = DangerousType.DISASTER
                }
            }
        }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        (dialog as AlertDialog).also {
            it.getButton(AlertDialog.BUTTON_NEGATIVE).also { button ->
                button.setTextColor(it.context.resources.getColor(R.color.gray))
                button.setOnClickListener { _ ->
                    it.dismiss()
                }
            }
            it.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { _ ->
                if (binding.descriptionEditText.text.isEmpty()) {
                    binding.inputLayout.error = null
                    binding.inputLayout.error = "input description"
                    binding.inputLayout.isErrorEnabled = true
                    return@setOnClickListener
                }
                viewModel.publish(Report(viewModel.description, viewModel.type))
                viewModel.description = ""
                it.dismiss()
            }
        }
    }

    companion object {
        data class Report(val description: String, val type: DangerousType)
    }
}