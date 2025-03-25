//Gabriel Pereira Faravola

public class MyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    private static class Node<T> {
        public int index;
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    public MyLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // Adiciona um elemento ao final da lista
    public T add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return data;
    }

    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    
    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fora do intervalo");
        }
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    public T get(int index) {
        Node<T> node = getNode(index);
        if (node != null) {
            return node.data;
        }
        throw new IndexOutOfBoundsException("Índice fora dos limites da lista."); // Caso o índice seja inválido
    }
    


    public void set(int index, T element) {
        getNode(index).data = element;
    }

    public void remove(int index) {
        Node<T> current = getNode(index);

        if (current == null) {
            System.out.println("Erro: Índice inválido.");
            return;
        }

        if(current.data != null){
            System.out.println(index+1+" "+ current.data);
        }

        current.data = (T) null;
    }


    // Limpa todos os elementos da lista
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // Retorna o tamanho da lista
    public int size() {
        return size;
    }

    // Verifica se a lista está vazia
    public boolean isEmpty() {
        return size == 0;
    }

    // Retorna o índice de um elemento específico na lista
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1; // Retorna -1 se o elemento não for encontrado
    }

    // Verifica se um elemento está presente na lista
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<T> current = head;
        int count = 0;
        while (current != null) {
            result.append(current.data);

            // Adiciona a vírgula se não for o último elemento
            if (current.next != null) {
                result.append(", ");
            }

            // Avança para o próximo nó
            current = current.next;
            count++;

            // Limita a exibição para 100 elementos
            if (count >= 100 && current != null) {
                result.append("...");
                break;
            }
        }
        result.append("]");
        return result.toString();
    }
}
