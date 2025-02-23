class Node:
    def __init__(self, key, parent=None):
        self.parent = parent
        self.left = None
        self.right = None
        self.min = self
        self.val = key
        self.n_v = 1

class BinarySearchTree:
    def __init__(self):
        self.root = None

    def insert(self, key):
        if self.root is None:
            self.root = Node(key)
            self.root.n_v = 1
        else:
            self._insert(self.root, key)

    def _insert(self, root, key):
        # if key < root.min.val:
            # root.min = key
            
        if key < root.val:
            if root.left is None:
                root.left = Node(key, root)
                root.min = root.left
                self._increment_ancestors(root.left)
            else:
                self._insert(root.left, key)
        else:
            if root.right is None:
                root.right = Node(key, root)
                self._increment_ancestors(root.right)
            else:
                self._insert(root.right, key)

    def _increment_ancestors(self, node):
        if node.parent == None:
            node.n_v += 1
        else:
            node.n_v += 1
            self._increment_ancestors(node.parent)

    def delete(self, key):
        self.root = self._delete(self.root, key)

    def _delete(self, root, key):
        if root is None:
            return root

        if key < root.val:
            root.left = self._delete(root.left, key)
        elif key > root.val:
            root.right = self._delete(root.right, key)
        else:
            if root.left is None:
                return root.right
            elif root.right is None:
                return root.left

            temp = self._min_value_node(root.right)
            # temp.min = root.min
            root.val = temp.val
            root.n_v -= 1
            root.right = self._delete(root.right, temp.val)

        return root

    def min(self):
        if self.root != None:
            return self.root.min.val
        
    def _min_value_node(self, node):
        current = node
        while current.left is not None:
            current = current.left
        return current

    def search(self, key):
        return self._search(self.root, key)

    def _search(self, root, key):
        if root is None or root.val == key:
            return root
        if key < root.val:
            return self._search(root.left, key)
        return self._search(root.right, key)

    def inorder(self):
        return self._inorder(self.root)

    def _inorder(self, root):
        res = []
        if root:
            res = self._inorder(root.left)
            res.append(root.val)
            res = res + self._inorder(root.right)
        return res

    def print_tree(self):
        lines, *_ = self._display_aux(self.root)
        for line in lines:
            print(line)

    def _display_aux(self, node):
        if node.right is None and node.left is None:
            line = f'{node.val} (n_v={node.n_v}) (minval={node.min.val})'
            width = len(line)
            height = 1
            middle = width // 2
            return [line], width, height, middle

        if node.right is None:
            lines, n, p, x = self._display_aux(node.left)
            s = f'{node.val} (n_v={node.n_v} (minval={node.min.val}))'
            u = len(s)
            first_line = (x + 1) * ' ' + (n - x - 1) * '_' + s
            second_line = x * ' ' + '/' + (n - x - 1 + u) * ' '
            shifted_lines = [line + u * ' ' for line in lines]
            return [first_line, second_line] + shifted_lines, n + u, p + 2, n + u // 2

        if node.left is None:
            lines, n, p, x = self._display_aux(node.right)
            s = f'{node.val} (n_v={node.n_v} (minval={node.min.val}))'
            u = len(s)
            first_line = s + x * '_' + (n - x) * ' '
            second_line = (u + x) * ' ' + '\\' + (n - x - 1) * ' '
            shifted_lines = [u * ' ' + line for line in lines]
            return [first_line, second_line] + shifted_lines, n + u, p + 2, u // 2

        left, n, p, x = self._display_aux(node.left)
        right, m, q, y = self._display_aux(node.right)
        s = f'{node.val} (n_v={node.n_v}) (minval={node.min.val})'
        u = len(s)
        first_line = (x + 1) * ' ' + (n - x - 1) * '_' + s + y * '_' + (m - y) * ' '
        second_line = x * ' ' + '/' + (n - x - 1 + u + y) * ' ' + '\\' + (m - y - 1) * ' '
        if p < q:
            left += [n * ' '] * (q - p)
        elif q < p:
            right += [m * ' '] * (p - q)
        zipped_lines = zip(left, right)
        lines = [first_line, second_line] + [a + u * ' ' + b for a, b in zipped_lines]
        return lines, n + m + u, max(p, q) + 2, n + u // 2
            
if __name__ == '__main__':
    bst = BinarySearchTree()

    for i in [12, 6, 7, 3, 5, 57, 38, 4]:
    # for i in [12, 57, 38]:
        bst.insert(i)
        bst.print_tree()

    # print("Deleting 38")
    # bst.delete(38)
    # bst.print_tree()

    # print("Deleting 7")
    # bst.delete(7)
    # bst.print_tree()

    # print("Deleting 12")
    # bst.delete(12)
    # bst.print_tree()
    
    # print("Deleting 3")
    # bst.delete(3)
    # bst.print_tree()

    print(f"Min: {bst.min()}")