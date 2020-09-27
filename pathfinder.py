import string

class Node:
    def __init__(self, name):
        self.adjacentVertices = set()
        self.wallVertices = set()
        self.name = name
    def addWallVertex(self, vertex):
        self.wallVertices.add(vertex)
        vertex.wallVertices.add(self)
    def getWallVertices(self):
        return self.wallVertices
    def addAdjacentVertex(self, vertex):
        self.adjacentVertices.add(vertex)
        vertex.adjacentVertices.add(self)
    def removeAdjacentVertex(self, vertex):
        self.adjacentVertices.remove(vertex)
        vertex.adjacentVertices.remove(self)
    def getAdjacentVertices(self):
        return self.adjacentVertices
    def __str__(self):
        return self.name
    __repr__ = __str__

graphArray = {}
nodesByName = {}

for letter in range(16): #Create every node on the map
    graphArray[letter] = {}
    for number in range(16):
        node = Node(str(string.ascii_lowercase[letter]) + str(number + 1))
        graphArray[letter][number] = node
        nodesByName[str(node)] = node

walls = {
    "h1" : ["i1"],
    "f2" : ["g2", "f3"],
    "h2" : ["i2"],
    "j2" : ["k2"],
    "d3" : ["e3"],
    "h3" : ["i3"],
    "l3" : ["m3"],
    "d4" : ["e4", "d5"],
    "l4" : ["m4", "l5"],
    "b5" : ["c5"],
    "d5" : ["e5"],
    "f5" : ["g5"],
    "n5" : ["o5"],
    "b6" : ["c6"],
    "f6" : ["g6"],
    "n6" : ["o6"],
    "b7" : ["c7"],
    "f7" : ["g7"],
    "j7" : ["k7"],
    "b8" : ["c8"],
    "f8" : ["g8"],
    "j8" : ["k8"],
    "j9" : ["k9"],
    "b10" : ["c10"],
    "f10" : ["g10"],
    "b11" : ["c11"],
    "f11" : ["g11"],
    "n11" : ["o11"],
    "b12" : ["c12"],
    "d12" : ["e12"],
    "m12" : ["n12", "m13"],
    "d13" : ["e13"],
    "f13" : ["g13"],
    "h13" : ["i13"],
    "k13" : ["l13"],
    "d14" : ["e14"],
    "f14" : ["g14", "f15"],
    "h14" : ["i14"],
    "d15" : ["e15"],
    "h15" : ["i15"],
    "k15" : ["l15"],
    "h16" : ["i16"],
    "k16" : ["l16"],
    "a9" : ["a10"],
    "b9" : ["b10"],
    "c4" : ["c5"],
    "c7" : ["c8"],
    "c9" : ["c10"],
    "c12" : ["c13"],
    "d7" : ["d8"],
    "d9" : ["d10"],
    "e2" : ["e3"],
    "e14" : ["e15"],
    "g4" : ["g5"],
    "g10" : ["g11"],
    "h6" : ["h7"],
    "h10" : ["h11"],
    "i6" : ["i7"],
    "i14" : ["i15"],
    "j6" : ["j7"],
    "j10" : ["j11"],
    "k2" : ["k3"],
    "k4" : ["k5"],
    "k6" : ["k7"],
    "k10" : ["k11"],
    "k12" : ["k13"],
    "l2" : ["l3"],
    "l6" : ["l7"],
    "l10" : ["l11"],
    "l12" : ["l13"],
    "m4" : ["m5"],
    "m8" : ["m9"],
    "n4" : ["n5"],
    "n8" : ["n9"],
    "n12" : ["n13", "o12"],
    "o6" : ["o7"],
    "o8" : ["o9"],
    "o10" : ["o11"],
    "p8" : ["p9"],
    "h7" : ["h8"], #middle box
    "h9" : ["h10"],
    "i7" : ["i8"],
    "i9" : ["i10", "j9"],
    "g8" : ["h8"],
    "i8" : ["j8"],
    "g9" : ["h9"],
}

for v1, vertexList in walls.items():
    for v2 in vertexList:
        nodesByName[v1].addWallVertex(nodesByName[v2])

for letter in graphArray: #Connect every adjacent node
    for number in graphArray[letter]:
        node = graphArray[letter][number]
        walls = node.getWallVertices()
        adjacentNodes = []
        top = graphArray[letter].get(number - 1, False)
        topRight = graphArray.get(letter + 1, {}).get(number - 1, False)
        right = graphArray.get(letter + 1, {}).get(number, False)
        bottomRight = graphArray.get(letter + 1, {}).get(number + 1, False)
        bottom = graphArray[letter].get(number + 1, False)
        bottomLeft = graphArray.get(letter - 1, {}).get(number + 1, False)
        left = graphArray.get(letter - 1, {}).get(number, False)
        topLeft = graphArray.get(letter - 1, {}).get(number - 1, False)
        
        if top and top not in walls:
            adjacentNodes.append(top)
        if right and right not in walls:
            adjacentNodes.append(right)
        if bottom and bottom not in walls:
            adjacentNodes.append(bottom)
        if left and left not in walls:
            adjacentNodes.append(left)
        if (
            (top and topRight and right)
            and not ( (top in walls) and (right in walls) )
            and not (top in topRight.getWallVertices() and right in topRight.getWallVertices())
            and not (top in walls and right in topRight.getWallVertices())
            and not (right in walls and top in topRight.getWallVertices())
        ):
            adjacentNodes.append(topRight)
        if (
            (right and bottomRight and bottom)
            and not ( (right in walls) and (bottom in walls) )
            and not (bottom in bottomRight.getWallVertices() and right in bottomRight.getWallVertices())
            and not (bottom in walls and right in bottomRight.getWallVertices())
            and not (right in walls and bottom in bottomRight.getWallVertices())
        ):
            adjacentNodes.append(bottomRight)
        if (
            (bottom and bottomLeft and left)
            and not ( (left in walls) and (bottom in walls) )
            and not (bottom in bottomLeft.getWallVertices() and left in bottomLeft.getWallVertices())
            and not (bottom in walls and left in bottomLeft.getWallVertices())
            and not (left in walls and bottom in bottomLeft.getWallVertices())
        ):
            adjacentNodes.append(bottomLeft)
        if (
            (left and topLeft and top)
            and not ( (left in walls) and (top in walls) )
            and not (top in topLeft.getWallVertices() and left in topLeft.getWallVertices())
            and not (top in walls and left in topLeft.getWallVertices())
            and not (left in walls and top in topLeft.getWallVertices())
        ):
            adjacentNodes.append(topLeft)
        for adjacentNode in adjacentNodes:
            node.addAdjacentVertex(adjacentNode)

def dfs(start,finish):
    stack = [start]
    visited = set()
    
    while stack:
        currentNode = stack[-1]
        if currentNode == finish:
            return stack
        visited.add(currentNode)
        unvisitedVertices = []
        for vertex in currentNode.getAdjacentVertices():
            if vertex not in visited:
                unvisitedVertices.append(vertex)
        if unvisitedVertices:
            stack.append(unvisitedVertices[0])
        else:
            stack.pop()
    return False

start = input("Enter start node:  ")
end = input("Enter end node:  ")
print(dfs(nodesByName[start],nodesByName[end]))

# print(dfs(nodesByName["a1"],nodesByName["p16"]))
# print("\n")
# print(dfs(nodesByName["a9"],nodesByName["p9"]))
# print("\n")
# print(dfs(nodesByName["c3"],nodesByName["j10"]))