package com.rodrigo.eventos.ui.checkIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rodrigo.eventos.R
import com.rodrigo.eventos.databinding.BottomSheetCheckInBinding
import com.tapadoo.alerter.Alerter
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventCheckInRegisterFragment : BottomSheetDialogFragment() {
    private lateinit var eventId: String

    private lateinit var binding: BottomSheetCheckInBinding
    private val viewModel: EventCheckInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            eventId = EventCheckInRegisterFragmentArgs.fromBundle(it).eventId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_check_in, container, false
        )

        binding.apply {
            this.viewModeCheckIn = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initObserver()
    }

    private fun initView() {
        binding.sendButton.setOnClickListener {
            viewModel.onSaveCheckIn(eventId)
        }
    }

    private fun initObserver() {
        viewModel.getEventStateView().observe(viewLifecycleOwner, {
            when (it) {
                is EventCheckInViewState.Loading -> displayLoading(it.isActive)
                is EventCheckInViewState.Checked -> displaySuccess()
                is EventCheckInViewState.Error -> showMessageError(it.message)
            }
        })
    }

    private fun displayLoading(active: Boolean) {
        binding.sendButton.isEnabled = false
        binding.progressBar.show()
    }

    private fun displaySuccess() {
        dismiss()
        Alerter.create(requireActivity())
            .setTitle(R.string.title_success)
            .setText(R.string.check_in_successful)
            .setBackgroundColorRes(R.color.colorPrimary)
            .setDuration(7000)
            .show()
    }

    private fun showMessageError(message: Int) {
        dismiss()
        Alerter.create(requireActivity())
            .setTitle(R.string.title_error)
            .setIcon(R.drawable.ic_baseline_report_24)
            .setBackgroundColorRes(R.color.colorDanger)
            .setText(message)
            .setDuration(7000)
            .show()
    }
}