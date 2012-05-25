import json, os, re

out = open('maps.js', 'w')

map_geo = {
    1: (160, 20),
    2: (121, 21),
    3: (50, 50),
    4: (100, 15)
}

for map_i in range(1,5):

    tiles = [
        tile 
        for line in open('map_%d.bb' % map_i, 'r').readlines()[18:298]
        for tile in re.findall(r'(\d{1,2})-\d', line)
    ]
    
    # map the ordered list into a dict('x,y'->tile_id)
    map = {'%d,%d' % (index % 160, index % 20): tile for index, tile in enumerate(tiles)}
    
    # print as js assignment to stdout
    out.write('\nmap_%d = %s' % (map_i, json.dumps(map)))

out.close()