import json, os, re


def cut_out(map_i, after, until):
    f = open('map_%d.bb' % map_i, 'r')
    start = False
    for line in f.readlines():
        if start and re.match(until, line):
            break
        if start:
            yield line
        if re.match(after, line):
            start = True
    f.close()

map_geo = {
    1: (160, 20),
    2: (121, 21),
    3: (50, 50),
    4: (100, 15)
}

bound_types = {
    0: 'free',
    4: 'solid',
    15: 'death',
    128: 'level_end'
}

for map_i in range(1,5):
    
    tiles = [
        tile 
        for line in cut_out(map_i, r'^\.level_texMapData%d$' % map_i, r'^$')
        for tile in re.findall(r'(\d{1,2})-\d', line)
    ]
    
    bounds = [
        bound
        for line in cut_out(map_i, r'^\.level_boundMapData%d$' % map_i, r'^$')
    ]

    
    # map the ordered list into a dict('x,y'->map_<i>_<tile id>)
    map = {
        'tiles': {'%d,%d' % (index % map_geo[map_i][0], index % map_geo[map_i][1]): 'map_%d_%s' % (map_i, tile) for index, tile in enumerate(tiles)}
    }
    
    # print as js assignment to stdout
    out = open('map_%d.js' % map_i, 'w')
    out.write('var map = %s;\n' % json.dumps(map))
    out.close()