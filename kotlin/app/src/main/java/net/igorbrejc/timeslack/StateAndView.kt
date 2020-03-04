package net.igorbrejc.timeslack

data class StateAndView<TState, TView>(val state: TState, val view: TView)