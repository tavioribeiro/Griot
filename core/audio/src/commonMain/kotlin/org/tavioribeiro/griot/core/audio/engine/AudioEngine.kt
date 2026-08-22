package org.tavioribeiro.griot.core.audio.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.tavioribeiro.griot.core.audio.model.ActivePlayer
import org.tavioribeiro.griot.core.audio.player.AudioPlayer
import org.tavioribeiro.griot.core.domain.annotation.model.SnippetRange

class AudioEngine(
    val mainPlayer: AudioPlayer,
    val snippetPlayer: AudioPlayer,
    private val scope: CoroutineScope,
) {

    private val _activePlayer = MutableStateFlow<ActivePlayer>(ActivePlayer.NONE)
    val activePlayer: StateFlow<ActivePlayer> = _activePlayer.asStateFlow()

    init {
        scope.launch {
            mainPlayer.state.collect { state ->
                if (state.isPlaying && _activePlayer.value == ActivePlayer.MAIN) {
                    snippetPlayer.pause()
                }
            }
        }
        scope.launch {
            snippetPlayer.state.collect { state ->
                if (state.isPlaying && _activePlayer.value == ActivePlayer.SNIPPET) {
                    mainPlayer.pause()
                }
            }
        }
    }

    fun playMain() {
        snippetPlayer.pause()
        mainPlayer.play()
        _activePlayer.value = ActivePlayer.MAIN
    }

    fun playSnippet(range: SnippetRange) {
        mainPlayer.pause()
        snippetPlayer.seekTo(range.startMs)
        snippetPlayer.play()
        _activePlayer.value = ActivePlayer.MAIN
    }

    fun pauseAll() {
        mainPlayer.pause()
        snippetPlayer.pause()
        _activePlayer.value = ActivePlayer.NONE
    }

    fun release() {
        mainPlayer.release()
        snippetPlayer.release()
    }
}