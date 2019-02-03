package sample

import kotlinx.cinterop.*
import platform.posix.*
import sdl.*

fun main(args: Array<String>) {
    Graphics.init()
    Graphics.run()
    Graphics.exit()
}

fun get_SDL_Error() = SDL_GetError()!!.toKString()

object Graphics {
    private var win:CPointer<SDL_Window>? = null
    private var ren:CPointer<SDL_Renderer>? = null
    private var bmp:CPointer<SDL_Surface>? = null
    private var tex:CPointer<SDL_Texture>? = null

    fun init() {
        println("start init")
        if(SDL_Init(SDL_INIT_EVERYTHING) == -1) {
            println(" !! ERROR !! SDL_Init: ${ get_SDL_Error() }")
        }
        win = SDL_CreateWindow("MyWindow", 0, 0, 640, 480, (SDL_WINDOW_SHOWN))
        if (win != null){
            println(" !! OK !! SDL_CreateWindow")
        }else{
            println(" !! ERROR !! SDL_CreateWindow: ${ get_SDL_Error() }" )
        }
        ren = SDL_CreateRenderer(win, -1, SDL_RENDERER_ACCELERATED or SDL_RENDERER_PRESENTVSYNC)
        if (ren != null){
            println(" !! OK !! SDL_CreateRenderer")
        }else{
            println(" !! ERROR !! SDL_CreateRenderer: ${ get_SDL_Error() }" )
        }
        println("SDL_Success: ${ get_SDL_Error() }" )
    }

    fun run(){
        var done = false
        memScoped {
            val event = alloc<SDL_Event>()
            val eventPtr:CValuesRef<SDL_Event> = event.ptr.reinterpret()
            while(!done){
                while (SDL_PollEvent(eventPtr) != 0) {
                    val eventType = event.type
                    if(eventType == SDL_QUIT || eventType == SDL_KEYDOWN) {
                        done = true
                    }
                }
            }
        }
    }

    fun exit() {
        SDL_DestroyRenderer(ren)
        SDL_DestroyWindow(win)
        println("Bye")
        SDL_Quit()
    }
}
