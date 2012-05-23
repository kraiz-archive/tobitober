Graphics 640,480, 16, 1

SetBuffer BackBuffer()
frametimer = CreateTimer (50)
frametimer_intro = createtimer(2)
ClsColor 0, 0, 0


;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	VARIABLEN DEKLARIEREN								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

;---BILDER-LADEN----------------------------------------------------------------																							
global tobi = LoadAnimImage("gfx/tobi.png", 32, 32, 0, 10)
MaskImage tobi, 255, 0, 255
global zwiebel = LoadanimImage("gfx/zwiebel.png", 32, 32, 0, 4)
maskimage zwiebel, 255, 0, 255
Global scoreboard = LoadImage("gfx/scoreboard.png")
maskimage scoreboard, 255, 0, 255
Global info_msg = LoadImage("gfx/info_msg.png")
maskimage info_msg, 255, 0, 255
global menu_zcrew = LoadImage("gfx/z-crew.png")
global menu_presents = LoadImage("gfx/presents.png")
global menu_header = LoadImage("gfx/header.png")
global menu_play = LoadImage("gfx/play.png")
global menu_exit = LoadImage("gfx/exit.png")

;---HINTERGRUENDE---------------------------------------------------------------
global background_map_4 = loadimage("gfx/background_map_4.png")

;---MONSTER-BILDER--------------------------------------------------------------
global monster_wasser = Loadanimimage("gfx/wasser.png", 32, 32, 0, 3)
maskimage monster_wasser, 255, 0, 255
global monster_schnappi = Loadanimimage("gfx/schnappi.png", 32, 32, 0, 7)
maskimage monster_schnappi, 255, 0, 255
global monster_wasserviech = Loadanimimage("gfx/wasserviech.png", 32, 32, 0, 7)
maskimage monster_wasserviech, 255, 0, 255
global monster_geist = Loadanimimage("gfx/geist.png", 32, 32, 0, 2)
maskimage monster_geist, 255, 0, 255
global monster_nsa = Loadanimimage("gfx/nsa.png", 32, 32, 0, 5)
maskimage monster_nsa, 255, 0, 255
global monster_robot = Loadanimimage("gfx/robot.png", 32, 32, 0, 4)
maskimage monster_robot, 255, 0, 255
global monster_zwiebli = Loadanimimage("gfx/zwiebli.png", 32, 32, 0, 2)
maskimage monster_zwiebli, 255, 0, 255
global monster_zwiablo = Loadanimimage("gfx/zwiablo.png", 160, 128, 0, 10)
maskimage monster_zwiablo, 255, 0, 255

;---SOUNDS----------------------------------------------------------------------
global sound_background_batman = loadsound("sfx/batman.mp3")
loopsound sound_background_batman

global sound_background_intro = loadsound("sfx/intro.mp3")

global sound_monster_schnappi = loadsound("sfx/schnappi.mp3")
global sound_monster_wasserviech = loadsound("sfx/wasserviech.mp3")
global sound_monster_nsa = loadsound("sfx/nsa.mp3")
global sound_monster_zwiebli = loadsound("sfx/zwiebli.mp3")

global sound_zwiebel = loadsound("sfx/zwiebel.mp3")

global sound_tobi_jump = loadsound("sfx/tobi_jump.mp3")
global sound_tobi_tot = loadsound("sfx/tobi_tot.mp3")
global sound_tobi_theone = loadsound("sfx/theone.mp3")

global channel_hintergrund

;---TOBI'S-GLOBALS--------------------------------------------------------------
Global tobi_pos_x = 0
Global tobi_pos_y = 0
Global tobi_jumptemp_y# = 0
Global tobi_frame = 0
Global tobi_anim_counter
Global tobi_punkte = 0
global tobi_leben = 3
global tobi_stop = false
global tobi_stop_counter = 0
global tobi_stop_tot = false
global tobi_stop_stageclear = false
global tobi_stop_gameend = false
global tobi_tot = false
global tobi_gameover = false
global tobi_passed_zwiebli = false
global tobi_hit_zwiablo = 0

Global jump
Global jumptimer
global stoptime, stoptime_temp, stoptime_min, stoptime_sec
global restart_level
global level = 1
Global intro_played = True ;###
global menu_load = 0
global menu_counter = 0
global kill_game = false

dim 	 map_breite(5), map_hoehe(5)
global tile_breite = 32, tile_hoehe = 32

dim level_texMap(5, 240, 240), level_boundMap(5, 240, 240)

;---ZWIEBEL-TYPES---------------------------------------------------------------
Global anzahl=0
Global info.zwiebel

Type zwiebel
	Field pos_x
	Field pos_y
	field frame
	field anim_counter
End Type

;---MONSTER_TYPES---------------------------------------------------------------
Global info2.monster

Type monster
	Field monstertyp
	Field pos_x
	Field pos_y
	Field richtung
	Field anim_counter
	Field frame
End Type

;---SCROLL-EBENEN---------------------------------------------------------------
Global scroll_x = 0, scroll_y = 64

;---INTRO-----------------------------------------------------------------------
global text$ = ReadFile ("data/scrolltext.txt")
global schriftrolle = Loadimage("gfx/schriftrolle.png")
maskimage schriftrolle, 255, 0, 255

global max_lines = 200    								
global text_spawn = 0
global lines_on_screen = 14
global eof_jump = 0
global eol_jump = 0
dim scrolltext$(max_lines)
dim x_offset(max_lines)


;---LEVELS----------------------------------------------------------------------


Include "data/map_1.bb"
include "data/map_2.bb"
include "data/map_3.bb"
include "data/map_4.bb"

map_breite(1) = 160
map_hoehe(1) = 20
map_breite(2) = 121
map_hoehe(2) = 21
map_breite(3) = 50
map_hoehe(3) = 50
map_breite(4) = 100
map_hoehe(4) = 15


Function GetTile (level, x, y)
      Return level_texMap (level, y, x) - 1
End Function

Function GetBounds (level, x, y)
      Return level_boundMap (level, y, x)
End Function

Function DrawTile (level, x, y, tile)
		if level = 1 then
      DrawImage tileset1, x, y, tile
		elseif level = 2 then
			drawimage tileset2, x, y, tile
		elseif level = 3 then
			drawimage tileset3, x, y, tile
		elseif level = 4 then
			drawimage tileset4, x, y, tile
		endif
End Function


;---TASTAURCODES----------------------------------------------------------------
Const rauf = 200, runter = 208, rechts = 205, links = 203, space = 57


;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	LEVEL-KRIMS-KRAMS								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

function LoadLevel()
	;---GLOBALS-AKTUALISIEREN-----------------------------------------------------
	tobi_pos_x = 320
	tobi_pos_y = 240
	tobi_jumptemp_y# = 0
	tobi_frame = 0
	tobi_tot = false
	tobi_stop_counter = 0
	tobi_stop = false
	tobi_stop_tot = false
	tobi_stop_stageclear = false
	tobi_gameover = false
	
	stoptime = 0
	stoptime_temp = 0
	stoptime_min = 0
	stoptime_sec = 0
	
	scroll_x = 0
	scroll_y = 96
	
	IF CHANNELPLAYING(channel_hintergrund) = 0 THEN
		channel_hintergrund = playsound(sound_background_batman)
	endif

	
	;---ZWIEBELPOSITIONEN-EINLESEN------------------------------------------------
	if level = 1 then
		Restore zwiebeldata1
	elseif level = 2 then
		restore zwiebeldata2
	elseif level = 3 then
		restore zwiebeldata3
	elseif level = 4 then
		restore zwiebeldata4
	endif
	Read anzahl
	For i = 1 To anzahl
		info.zwiebel = New zwiebel
		Read x, y
		info\pos_x = x * tile_breite
		info\pos_y = y * tile_hoehe
		info\frame = 0
		info\anim_counter = 0
	Next

	;---MONSTERPOSITIONEN-EINLESEN------------------------------------------------
	if level = 1 then
		Restore monsterdata1
	elseif level = 2 then
		Restore monsterdata2
	elseif level = 3 then
		Restore monsterdata3
	elseif level = 4 then
		Restore monsterdata4
	endif	
	Read anzahl
	For i = 1 To anzahl						
		info2.monster = New monster			
		Read x, y, monstertyp, richtung	
		info2\monstertyp = monstertyp	
		info2\pos_x = x * tile_breite
		info2\pos_y = y * tile_hoehe
		info2\richtung = richtung
		info2\anim_counter = 0
		info2\frame = 0
	Next 	
end function

Function UnLoadLevel()
	;---ZWIEBELPOSITIONEN-LOESCHEN------------------------------------------------
	For info.zwiebel = Each zwiebel
		delete info.zwiebel
	Next
	
	;---MONSTERPOSITIONEN-LOESCHEN------------------------------------------------
	For info2.monster = Each monster
		delete info2.monster
	Next
	
	cls
end function


;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	NEUEN FRAME MALEN  								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

;---LEVELTEXTUREN-MALEN---------------------------------------------------------
Function DrawFrame()
	cls
	
	;---Hintergrund
	if level = 1 then
		
	elseif level = 2 then
	
	elseif level = 4 then
	   tileblock background_map_4, -scroll_x , (-50 -scroll_y)
	endif

	;---Texturen
	For y = 0 To map_hoehe(level)
		For x = 0 To map_breite(level)
		 	DrawTile (level, (x * tile_breite) - scroll_x, (y * tile_hoehe) - scroll_y, level_texMap(level, y, x))
		 Next
	Next
	
	;---Zwiebeln
	For info.zwiebel = Each zwiebel
		DrawImage zwiebel, info\pos_x - scroll_x, info\pos_y - scroll_y, info\frame
	Next
	
	;---Tobi
	DrawImage tobi, tobi_pos_x, tobi_pos_y, tobi_frame

	;---Monster
	For info2.monster = Each monster
		;wasser
		If info2\monstertyp = 1 Then
			DrawImage monster_wasser, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\richtung
		
		;schnappi
		elseIf info2\monstertyp = 2 Then
			DrawImage monster_schnappi, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame
		
		;wasserviech
		elseif info2\monstertyp = 3 then
			DrawImage monster_wasserviech, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame
			
		;geist
		elseif info2\monstertyp = 4 then
			DrawImage monster_geist, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame

    ;nsa
		elseif info2\monstertyp = 5 then
			DrawImage monster_nsa, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame		
		
    ;robot
		elseif info2\monstertyp = 6 then
			DrawImage monster_robot, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame		
			
		;zwiebli
		elseif info2\monstertyp = 7 then
			DrawImage monster_zwiebli, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame
			
		;zwiablo
		elseif info2\monstertyp = 8 then
			DrawImage monster_zwiablo, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame
			
		endif
	Next
	
	;zwiebli in level 3
	if level = 3 then
	  drawimage monster_zwiebli, (8*32) - scroll_x, (46*32) - scroll_y
	endif
	
End Function



;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	TOBI TOBER  								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

;---LAUFEN----------------------------------------------------------------------
Function MoveTobi()
;-links
	If Keydown(links) Then	
		If GetBounds (level, (tobi_pos_x + scroll_x) /32, (tobi_pos_y +16 + scroll_y) /32) = 0 Then	
			If scroll_x =< -312 Then 	
				scroll_x = -312
			Else 						
				scroll_x = scroll_x  -4
			EndIf
		EndIf
		
		if tobi_anim_counter >= 4 then			
			If jump = True Then
		 	 tobi_frame = 8
			ElseIf tobi_frame <= 3 Then
				tobi_frame = 4
			EndIf
			
			if tobi_frame >= 4 and jump = false then
			  tobi_frame = tobi_frame +1
			  if tobi_frame >= 8 then tobi_frame = 4	
			EndIf		
			If tobi_pos_x =< 0 Then tobi_pos_x = 1
			
			tobi_anim_counter = 0
	 	else
	 		tobi_anim_counter = tobi_anim_counter +1
	 	endif
	 	
	endif

;-rechts	
	If KeyDown(rechts) Then
		If GetBounds (level, ((tobi_pos_x +32) + scroll_x) /32, ((tobi_pos_y +16) + scroll_y) /32) = 0 Then
			If scroll_x => map_breite(level) *tile_breite Then
				scroll_x = map_breite(level) *tile_breite-4
			Else
				scroll_x = scroll_x +4
			EndIf
		endif
		
		if tobi_anim_counter >= 4 then
			If jump = True Then
			  tobi_frame = 9
			 elseif tobi_frame >= 3
				tobi_frame = 0
			EndIf
		
			if tobi_frame <= 3 and jump = false then
		  	tobi_frame = tobi_frame +1
		  	if tobi_frame >= 4 then tobi_frame = 0
	  	EndIf
	  	
			tobi_anim_counter = 0
	 	else
	 		tobi_anim_counter = tobi_anim_counter +1
	 	endif 			
	
	elseIf jump = False Then		
		if tobi_frame = 9 then tobi_frame = 0
		if tobi_frame = 8 then tobi_frame = 4
	EndIf
		
End Function

;---SPRINGEN--------------------------------------------------------------------
Function DoJump()
	;-Tastenabfrage
	If KeyHit(space) Then
		If jump = False then
			jump = True
			tobi_jumptemp_y = 12
			tobi_sound = playsound(sound_tobi_jump)
		EndIf
	EndIf
	
	;-Sprungroutine
	If jump = True Then
	  if tobi_jumptemp_y >= -16 then
			tobi_jumptemp_y = tobi_jumptemp_y -0.5
		endif
		scroll_y = scroll_y - tobi_jumptemp_y
		If tobi_jumptemp_y < 0 Then
			If GetBounds(level, ((tobi_pos_x +16) +scroll_x) /32, ((tobi_pos_y +32) + scroll_y) /32) <> 0 Then
				jump = false
				temp = (tobi_pos_y + scroll_y) /32
				scroll_y = (temp *32) - tobi_pos_y
				tobi_passed_zwiebli = false
			EndIf
		EndIf
	EndIf

	
	;-Stopp falls tobi irgendwo von unten gegenspringt
	If GetBounds (level, ((tobi_pos_x +16) + scroll_x) /32 , (tobi_pos_y + scroll_y) /32) <> 0 And jump = True Then
		if GetBounds (level, ((tobi_pos_x +16) + scroll_x) /32 , (tobi_pos_y + scroll_y) /32) <> 1 then			
			jump = False
			temp = (tobi_pos_y + scroll_y +31) /32
			scroll_y = (temp *32) - tobi_pos_y
		endif
	EndIf
	
	;-Gravity
	If jump = False  Then
		If GetBounds (level, ((tobi_pos_x +16) + scroll_x) /32, ((tobi_pos_y +32) + scroll_y) /32) = 0 Then
			jump = True
			tobi_jumptemp_y = 0
		EndIf
	EndIf 	
End Function


;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	MONSTER + ZWIEBELN  								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

;---BEWEGUNG--------------------------------------------------------------------
Function MoveMonster()
	;-zwiebel-'wackling'
	for info.zwiebel = each zwiebel
		if info\anim_counter = 7 then
			info\frame = info\frame +1
			if info\frame = 4 then info\frame = 0
			info\anim_counter = 0
		else
			info\anim_counter = info\anim_counter +1
		endif
	next

	For info2.monster = Each monster
		
		;-Wasser
		If info2\monstertyp = 1 Then 	
			if info2\anim_counter >= 7 then
				info2\richtung = info2\richtung +1
				if info2\richtung = 3 then info2\richtung = 0		
				info2\anim_counter = 0   	
			else
				info2\anim_counter = info2\anim_counter +1
			endif
		
			
		;-schnappi & wasserviech
		elseIf info2\monstertyp = 2 or info2\monstertyp = 3 Then 	
			If info2\richtung = 1 Then
				info2\pos_x = info2\pos_x +2
				If GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +32) /32) = 0 Then	
					info2\richtung = 0
					info2\frame = 3
				elseIf GetBounds(level, (info2\pos_x +32) /32, (info2\pos_y +16) /32) <> 0 then
				  info2\richtung = 0
				  info2\frame = 3
				endif
				
				if info2\anim_counter >= 7 then
					info2\frame = info2\frame +1
					if info2\frame = 3 then info2\frame = 0		
					info2\anim_counter = 0   	
				else
			  	info2\anim_counter = info2\anim_counter +1
			  endif
			  
			ElseIf info2\richtung = 0 Then
				info2\pos_x = info2\pos_x -2
				If GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +32) /32) = 0 Then
					info2\richtung = 1
					info2\frame = 0
				elseIf GetBounds(level, (info2\pos_x) /32, (info2\pos_y +16) /32) <> 0 then
					info2\richtung = 1
					info2\frame = 0			
				EndIf
				
				if info2\anim_counter >= 7 then
					info2\frame = info2\frame +1
					if info2\frame = 6 then info2\frame = 3		
					info2\anim_counter = 0   	
				else
					info2\anim_counter = info2\anim_counter +1
				endif
				
			elseif info2\richtung = 2 then
			  info2\frame = 6
			  if info2\anim_counter >= 50 then
					delete info2.monster
				else
					info2\anim_counter = info2\anim_counter +1
				endif				
			endif

		
		;-geist
		elseif info2\monstertyp = 4 Then
			If info2\pos_x - scroll_x < tobi_pos_x Then
						info2\pos_x = info2\pos_x +1
						info2\richtung = 1
			ElseIf info2\pos_x - scroll_x > tobi_pos_x Then
						info2\pos_x = info2\pos_x -1
						info2\richtung = 0			
			EndIf
			If info2\pos_y - scroll_y < tobi_pos_y Then
						info2\pos_y = info2\pos_y +1
			ElseIf info2\pos_y - scroll_y > tobi_pos_y Then
						info2\pos_y = info2\pos_y -1
			EndIf
			
			if info2\anim_counter >= 7 then
				info2\frame = info2\frame +1
				if info2\frame = 2 then info2\frame = 0		
				info2\anim_counter = 0   	
			else
				info2\anim_counter = info2\anim_counter +1
			endif
			
		;-nsa & robot
		elseIf info2\monstertyp = 5 or info2\monstertyp = 6 Then 	
			If info2\richtung = 1 Then
				if info2\anim_counter >= 7 then
					info2\frame = info2\frame +1
					if info2\frame = 2 then info2\frame = 0		
					info2\anim_counter = 0   	
				else
			  	info2\anim_counter = info2\anim_counter +1
			  endif
			  
				info2\pos_x = info2\pos_x +2
				If GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +32) /32) = 0 or GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +32) /32) = 15 Then	
					info2\richtung = 0
					info2\frame = 2
				elseIf GetBounds(level, (info2\pos_x +32) /32, (info2\pos_y +16) /32) <> 0 then
				  info2\richtung = 0
				  info2\frame = 2
				endif
			
			ElseIf info2\richtung = 0 Then
				if info2\anim_counter >= 7 then
					info2\frame = info2\frame +1
					if info2\frame = 4 then info2\frame = 2		
					info2\anim_counter = 0   	
				else
					info2\anim_counter = info2\anim_counter +1
				endif
				
				info2\pos_x = info2\pos_x -2
				If GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +32) /32) = 0 or GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +32) /32) = 15 Then
					info2\richtung = 1
					info2\frame = 0
				elseIf GetBounds(level, (info2\pos_x) /32, (info2\pos_y +16) /32) <> 0 then
					info2\richtung = 1
					info2\frame = 0			
				EndIf
				
			elseif info2\richtung = 2 then
			  info2\frame = 4
			  if info2\anim_counter >= 30 then
					delete info2.monster
				else
					info2\anim_counter = info2\anim_counter +1
				endif				
			endif
			
		;-zwiebli	
		elseIf info2\monstertyp = 7 Then 	
			If (info2\pos_x - scroll_x) < (tobi_pos_x -32) Then
				info2\pos_x = info2\pos_x +3
				info2\richtung = 1
				info2\frame = 0
			ElseIf (info2\pos_x - scroll_x) > (tobi_pos_x +32) Then
				info2\pos_x = info2\pos_x -3
				info2\richtung = 0
				info2\frame = 1			
			EndIf
			
			If (info2\pos_y - scroll_y) < (tobi_pos_y -32) Then
				info2\pos_y = info2\pos_y +2
			ElseIf (info2\pos_y - scroll_y) > (tobi_pos_y -32) Then
				info2\pos_y = info2\pos_y -2
			EndIf
			
		;-zwiablo
		elseIf info2\monstertyp = 8 Then 	
			If info2\richtung = 1 Then
				if info2\anim_counter >= 7 then
					info2\frame = info2\frame +1
					if info2\frame = 3 then info2\frame = 0		
					info2\anim_counter = 0   	
				else
			  	info2\anim_counter = info2\anim_counter +1
			  endif
			
				info2\pos_x = info2\pos_x +1
				If GetBounds(level, (info2\pos_x +160) /32, (info2\pos_y +128) /32) = 0 Then	
					info2\richtung = 0
					info2\frame = 3
				elseIf GetBounds(level, (info2\pos_x +160) /32, (info2\pos_y +112) /32) <> 0 then
				  info2\richtung = 0
				  info2\frame = 3
				endif
			
			ElseIf info2\richtung = 0 Then
				if info2\anim_counter >= 7 then
					info2\frame = info2\frame +1
					if info2\frame = 6 then info2\frame = 3		
					info2\anim_counter = 0   	
				else
					info2\anim_counter = info2\anim_counter +1
				endif
				
				info2\pos_x = info2\pos_x -1
				If GetBounds(level, (info2\pos_x +16) /32, (info2\pos_y +128) /32) = 0 Then
					info2\richtung = 1
					info2\frame = 0
				elseIf GetBounds(level, (info2\pos_x) /32, (info2\pos_y +112) /32) <> 0 then
					info2\richtung = 1
					info2\frame = 0			
				EndIf
				
			elseif info2\richtung = 2 then
			  if info2\anim_counter >= 30 then
					info2\richtung = 0
					info2\frame = 3
					if tobi_hit_zwiablo = 3 then
						tobi_stop = true
						tobi_stop_gameend = true
					endif
				else
					info2\anim_counter = info2\anim_counter +1
					if info2\frame = 7 then
						info2\frame = 9
					else
						info2\frame = 7
					endif
				endif				
			
			elseif info2\richtung = 3 then
			  if info2\anim_counter >= 30 then
					info2\richtung = 1
					info2\frame = 0
					if tobi_hit_zwiablo = 3 then
						tobi_stop = true
						tobi_stop_gameend = true
					endif
				else
					info2\anim_counter = info2\anim_counter +1
					if info2\frame = 6 then
						info2\frame = 8
					else
						info2\frame = 6
					endif
				endif				
			endif
			
		EndIf
		
	Next
End Function


;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	KOLLISIONSABFRAGEN  								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

;---KOLLISION-------------------------------------------------------------------
Function Kollision()
	;-mit Zwiebeln
	For info.zwiebel = Each zwiebel
		If ImagesCollide (zwiebel, info\pos_x - scroll_x, info\pos_y - scroll_y, 0, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
			Delete info.zwiebel
			tobi_punkte = tobi_punkte + 10
			channel = playsound(sound_zwiebel)
		endif
	Next
	
	;-mit Monstern	
	For info2.monster = Each monster
		If info2\monstertyp = 2 Then
			;-schnappi
			if info2\richtung <> 2 then
				If ImagesCollide (monster_schnappi, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
					if jump = true and tobi_jumptemp_y < 0 then
						info2\anim_counter = 0
						info2\richtung = 2
						tobi_punkte = tobi_punkte + 25
						tobi_jumptemp_y = 3
						channel = playsound(sound_monster_schnappi)
					else
						tobi_stop = true
						tobi_stop_tot = true
						tobi_stop_stageclear = false
					endif
				endif
			EndIf	
		elseIf info2\monstertyp = 3 Then
			;-wasserviech
			if info2\richtung <> 2 then
				If ImagesCollide (monster_wasserviech, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
					if jump = true and tobi_jumptemp_y < 0 then
						info2\anim_counter = 0
						info2\richtung = 2
						tobi_punkte = tobi_punkte + 25
						tobi_jumptemp_y = 3
						channel = playsound(sound_monster_wasserviech)
					else
						tobi_stop = true
						tobi_stop_tot = true
						tobi_stop_stageclear = false
					endif
				endif
			EndIf
		
		elseIf info2\monstertyp = 4 Then
			;-geist
			if info2\richtung <> 2 then
				If ImagesCollide (monster_geist, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
						tobi_stop = true
						tobi_stop_tot = true
						tobi_stop_stageclear = false
				endif
			EndIf

		elseIf info2\monstertyp = 5 Then
			;-nsa
			if info2\richtung <> 2 then
				If ImagesCollide (monster_nsa, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
					if jump = true and tobi_jumptemp_y < 0 then
						info2\anim_counter = 0
						info2\richtung = 2
						tobi_punkte = tobi_punkte + 25
						tobi_jumptemp_y = 3
						channel = playsound(sound_monster_nsa)
					else
						tobi_stop = true
						tobi_stop_tot = true
						tobi_stop_stageclear = false
					endif
				endif
			EndIf
			
		elseIf info2\monstertyp = 6 Then
			;-robot
			if info2\richtung <> 2 then
				If ImagesCollide (monster_robot, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
						tobi_stop = true
						tobi_stop_tot = true
						tobi_stop_stageclear = false
				endif
			EndIf
			
  	elseIf info2\monstertyp = 7 Then
			;-zwiebli
			if info2\richtung <> 2 then
				If ImagesCollide (monster_geist, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
					if jump = true and tobi_jumptemp_y < 0 and tobi_passed_zwiebli = false then
						tobi_jumptemp_y = 11
						channel = playsound(sound_monster_zwiebli)
						tobi_passed_zwiebli = true
					endif
				endif
			EndIf
			
  	elseIf info2\monstertyp = 8 Then
			;-zwiablo
			if info2\richtung <> 2 and info2\richtung <> 3 then
				If ImagesCollide (monster_zwiablo, info2\pos_x - scroll_x, info2\pos_y - scroll_y, info2\frame, tobi, tobi_pos_x, tobi_pos_y, tobi_frame) Then
					if jump = true and tobi_jumptemp_y < 0 and tobi_passed_zwiebli = true then
						info2\anim_counter = 0
						if info2\richtung = 0 then
							info2\richtung = 2
						elseif info2\richtung = 1 then
							info2\richtung = 3
						endif
						tobi_punkte = tobi_punkte + 500
						tobi_jumptemp_y = 10
						tobi_hit_zwiablo = tobi_hit_zwiablo +1
						tobi_passed_zwiebli = false
						;channel = playsound(sound_monster_zwiablo)
					else
						tobi_stop = true
						tobi_stop_tot = true
						tobi_stop_stageclear = false
					endif
				endif
			EndIf
					
		EndIf
	Next
	
	;-mit Todes-Bound
	if GetBounds (level, ((tobi_pos_x +16) + scroll_x) /32, ((tobi_pos_y +32) + scroll_y) /32) = 15 then
		tobi_stop = true
		tobi_stop_tot = true
		tobi_stop_stageclear = false
	endif
	
		;-mit Levelende
	if GetBounds (level, ((tobi_pos_x +32) + scroll_x) /32, ((tobi_pos_y +16) + scroll_y) /32) = 128 or GetBounds (level, ((tobi_pos_x -1) + scroll_x) /32, ((tobi_pos_y) + scroll_y) /32) = 128 or GetBounds (level, ((tobi_pos_x +16) + scroll_x) /32, ((tobi_pos_y +32) + scroll_y) /32) = 128 then
		tobi_stop = true
		tobi_stop_stageclear = true
		tobi_stop_tot = false
	endif
End Function





;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	SCORE-BOARD 								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

;-mitlaufende zeit
function stoptimer()
	stoptime_temp = stoptime_temp +1
	if stoptime_temp = 50 then
		stoptime = stoptime +1
		stoptime_temp = 0
	endif
	
	stoptime_min = stoptime / 60
	stoptime_sec = stoptime MOD 60
end function

;-meldung bei tod
function todesanim()
	tobi_stop_counter = tobi_stop_counter +1
	if tobi_stop_counter = 10 then
		channel = playsound(sound_tobi_tot)
		drawimage info_msg, 180, 130
		text 220, 145, "olala, du bist tot!"
		if tobi_leben = 1 then
			text 220, 165, "jetz hast es vergeigt!"
		else
			text 220, 165, "hast noch " + (tobi_leben -1) + " leben."
		endif
		text 220, 195, "weiter mit <space>"
		flip
		repeat
		until keyhit(space)
		tobi_tot = true
	endif	
end function

;-meldung bei levelende
function stageclear()
	tobi_stop_counter = tobi_stop_counter +1
	if tobi_stop_counter = 10 then
		drawimage info_msg, 180, 130
		text 220, 145, "stage clear!"
		text 220, 165, "ab gehtz ins nexte."
		text 220, 195, "weiter mit <space>"
		flip
		repeat
		until keyhit(space)
		;test
		level = level +1
		restart_level = true
	endif	
end function

;-meldung bei spielende
function gameend()
	channel = playsound(sound_tobi_theone)
	drawimage info_msg, 180, 130
	text 220, 145, "bravo, du hast die Welt"
	text 220, 165, "gerettet, zumindest für"
	text 220, 195, "heute :)"
	flip
	repeat
	until keyhit(space)
	tobi_gameover = true
	flushkeys	
end function

;-meldung bei gameover
function gameover()
	if tobi_tot = true then
		tobi_leben = tobi_leben -1
		restart_level = true
		tobi_tot = false
		tobi_stop = false
		tobi_stop_counter = 0
	endif	
	if tobi_leben = 0 then
		tobi_gameover = true
		level = 1
	endif
end function

;-statusleiste
Function DrawBoard()
	DrawImage scoreboard, 0, 0
	if level = 1 then
		text 10, 9, "Strand Hawaii"
	elseif level = 2 then
		text 10, 9, "Kreuzfahrtschiff"
	elseif level = 3 then
		text 10, 9, "Area51 Bunker"
	elseif level = 4 then
		text 10, 9, "Märzdorf"
	endif
	text 209, 9, "Punkte: " + tobi_punkte
	text 434, 9, "Leben: " + tobi_leben
	text 537, 9, "Zeit:"
	if stoptime_sec < 10 then
		text 590, 9, stoptime_min + ":" + "0" + stoptime_sec
	else
		text 590, 9, stoptime_min + ":" + stoptime_sec
	endif
end function



;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	Story-Intro								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

function story()
	font=loadfont("times new roman", 20)
	setfont font
	color 0, 0, 0

	;zeilen löschen
	For i=1 To lines_on_screen
 		scrolltext$(i) = ""
 		i = i+1
	Next
	a = i

	;feld füllen
	Repeat														
 		scrolltext$(i)=ReadLine ( Text$ )
  	If scrolltext$(i) = "EOF" Then
  		CloseFile ( Text$ )
  		Text$ = ReadFile ("data/scrolltext.txt")
   		eol_jump = 1
   		scrolltext$(i) = ""
  	EndIf
  	a = Len ( scrolltext$(i) )
		x_offset(i) = ( (640/2) - (a*3.5) )
  	i = i+1	
	Until eol_jump = 1	
	a = i

	;text scrollen
	channel = playsound(sound_background_intro)
	freetimer frametimer
	frametimer = createtimer (23)	
	repeat
		For eol_jump = 30 To 1 Step -1
			drawimage schriftrolle, 0, 0 									
  		For i = 1 To lines_on_screen
    		Text x_offset(i+eof_jump),text_spawn+(30*i+eol_jump),scrolltext$(i+eof_jump)
   		Next
			waittimer (frametimer)													
   		Flip													
   		Cls													
		Next																										
		eof_jump=eof_jump+1										
  	If eof_jump=a-4 Then exit
	until eof_jump=a-4
	freetimer frametimer
	frametimer = createtimer (50)	
	freefont font
	color 255,255,255
end function














;<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
;  	HAUPT-SCHLEIFE								
;>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

repeat

	if keyhit(28) or keyhit(25) then
		if intro_played = false then
			story()
			FLUSHKEYS
			intro_played = true
		endif
		tobi_leben = 3
		tobi_punkte = 0
		
		LoadLevel()

		Repeat
			WaitTimer (frametimer)
			
			if restart_level = true then
				restart_level = false
				UnLoadLevel()
				LoadLevel()
			endif

      MoveMonster()
      Kollision()
      
      if tobi_stop = false then
				MoveTobi()
				DoJump()
			else
				if tobi_stop_tot = true then
					todesanim()
				elseif tobi_stop_stageclear = true then
					stageclear()
				elseif tobi_stop_gameend = true then
					gameend()
				endif
			endif
			
			stoptimer()
			gameover()
			
			Drawframe()
			DrawBoard()

			Flip

		Until KeyHit (1) or tobi_gameover = true or tobi_gameend = true
		
		UnLoadLevel()
		flushkeys
	endif

	;Menü
	cls
	if menu_load > 2 then drawimage menu_zcrew, 240, 10
	if menu_load > 3 then drawimage menu_presents, 190, 50
	if menu_load > 7 then drawimage menu_header, 170, 150
	if menu_load > 10 then drawimage menu_play, 242, 300
	if menu_load > 11 then drawimage menu_exit, 242, 350
	
	if menu_load < 15 then
		if menu_counter >= 25 then
			menu_load = menu_load +1
			menu_counter = 0
		else
			menu_counter = menu_counter +1
		endif
	endif
	
	WaitTimer (frametimer)
	
	flip

until keyhit(1) or keyhit(18)

;-speicher leeren
freesound sound_monster_schnappi
freesound sound_monster_wasserviech
freesound sound_monster_nsa
freesound sound_monster_zwiebli
freesound sound_zwiebel
freesound sound_tobi_jump
freesound sound_tobi_tot
freesound sound_background_batman
freesound sound_background_intro

freeImage tobi
freeImage zwiebel
freeImage scoreboard
freeImage info_msg
freeimage schriftrolle
freeimage menu_z-crew
freeimage menu_presents
freeimage menu_header
freeimage menu_play
freeimage menu_exit
freeImage monster_wasser
freeImage monster_schnappi
freeImage monster_wasserviech
freeImage monster_geist
freeImage monster_nsa
freeimage monster_robot
freeImage monster_zwiebli
freeimage monster_zwiablo
freeimage tileset1
freeimage tileset2
freeimage tileset3
freeimage tileset4
freeimage background_map_4

end

