import turtle


def setup_turtle():
    turtle.setup(width=600, height=600)
    turtle.bgcolor("sky blue")
    turtle.speed("fastest")
    turtle.penup()
    turtle.setheading(90)
    turtle.goto(0, -250)
    turtle.pendown()


def generate_word(word, iterations):
    for _ in range(iterations):
        new_word = "".join(rules.get(symbol, symbol) for symbol in word)
        word = new_word
    return word


def draw_word(word):
    stack = []
    leaf_positions = []

    for symbol in word:
        if symbol == "F":
            turtle.forward(5)
        elif symbol == "+":
            turtle.right(angle)
        elif symbol == "-":
            turtle.left(angle)
        elif symbol == "[":
            stack.append((turtle.position(), turtle.heading()))
        elif symbol == "]":
            position, heading = stack.pop()
            turtle.penup()
            turtle.goto(position)
            turtle.setheading(heading)
            turtle.pendown()
        elif symbol == "X":
            leaf_positions.append(turtle.position())

    turtle.color("green")
    turtle.penup()
    for position in leaf_positions:
        turtle.goto(position)
        turtle.dot(7)
    turtle.pendown()


symbols = "XF+-[]"
rules = {"X": "F+[[X]-X]-F[-FX]+X", "F": "FF"}
angle = 25
initial_word = "X"
iterations = 5

setup_turtle()
word = generate_word(initial_word, iterations)
draw_word(word)

turtle.hideturtle()
turtle.done()
