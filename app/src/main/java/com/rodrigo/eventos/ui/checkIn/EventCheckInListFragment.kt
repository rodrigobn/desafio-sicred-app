package com.rodrigo.eventos.ui.checkIn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rodrigo.eventos.R
import com.rodrigo.eventos.data.model.Event
import com.rodrigo.eventos.databinding.EventCheckInListFragmentBinding
import com.rodrigo.eventos.ui.adapter.EventListAdapter
import com.tapadoo.alerter.Alerter
import koleton.api.loadSkeleton
import kotlinx.android.synthetic.main.no_data_message.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventCheckInListFragment : Fragment(), EventListAdapter.OnItemClickListener {

    private lateinit var binding: EventCheckInListFragmentBinding

    private val viewModel: EventCheckInViewModel by viewModel()
    private val eventListAdapter = EventListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.event_check_in_list_fragment, container, false
        )

        binding.apply {
            this.adapter = eventListAdapter
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        viewModel.loadCheckedEvents()
    }

    private fun initView() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.title_check_in)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadCheckedEvents()
        }
        initEventStateView()
    }

    private fun initEventStateView() {
        viewModel.getEventStateView().observe(viewLifecycleOwner, {
            when (it) {
                is EventCheckInViewState.Loading -> displayLoading(it.isActive)
                is EventCheckInViewState.ShowData -> it.events.let(eventListAdapter::submitList)
                is EventCheckInViewState.NoData -> displayNoData()
                is EventCheckInViewState.Error -> showMessageError(it.message)
            }
        })
    }

    private fun displayLoading(isActive: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = false

        when (isActive) {
            true -> {
                binding.placeholder.loadSkeleton()
                binding.placeholder.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
                binding.boxNoData.visibility = View.GONE
            }
            else -> {
                binding.placeholder.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun displayNoData() {
        binding.boxNoData.visibility = View.VISIBLE
        binding.placeholder.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.boxNoData.messageNoData.setText(R.string.message_no_check_in)
    }

    private fun showMessageError(message: Int) {
        Alerter.create(requireActivity())
            .setTitle(R.string.title_error)
            .setIcon(R.drawable.ic_baseline_report_24)
            .setBackgroundColorRes(R.color.colorDanger)
            .setText(message)
            .setDuration(7000)
            .show()
    }

    override fun onEventClick(event: Event) {
        val action = EventCheckInListFragmentDirections.toEventDetail(event)
        findNavController().navigate(action)
    }
}