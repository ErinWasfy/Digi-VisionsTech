PGDMP         (                }           bookingqueryoptimization    12.18    12.18                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                        0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            !           1262    16393    bookingqueryoptimization    DATABASE     �   CREATE DATABASE bookingqueryoptimization WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
 (   DROP DATABASE bookingqueryoptimization;
                postgres    false            �            1259    16479    bookings    TABLE     �   CREATE TABLE public.bookings (
    id bigint NOT NULL,
    property_id bigint NOT NULL,
    user_id bigint NOT NULL,
    start_date date NOT NULL,
    end_date date NOT NULL
);
    DROP TABLE public.bookings;
       public         heap    postgres    false            �            1259    16477    bookings_id_seq    SEQUENCE     �   ALTER TABLE public.bookings ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.bookings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    207            �            1259    16469 
   properties    TABLE     j   CREATE TABLE public.properties (
    id bigint NOT NULL,
    title text NOT NULL,
    owner_id integer
);
    DROP TABLE public.properties;
       public         heap    postgres    false            �            1259    16467    properties_id_seq    SEQUENCE     �   ALTER TABLE public.properties ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.properties_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    205            �            1259    16428    users    TABLE     N   CREATE TABLE public.users (
    id bigint NOT NULL,
    name text NOT NULL
);
    DROP TABLE public.users;
       public         heap    postgres    false            �            1259    16426    users_id_seq    SEQUENCE     �   ALTER TABLE public.users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);
            public          postgres    false    203                      0    16479    bookings 
   TABLE DATA           R   COPY public.bookings (id, property_id, user_id, start_date, end_date) FROM stdin;
    public          postgres    false    207   �                 0    16469 
   properties 
   TABLE DATA           9   COPY public.properties (id, title, owner_id) FROM stdin;
    public          postgres    false    205   �                 0    16428    users 
   TABLE DATA           )   COPY public.users (id, name) FROM stdin;
    public          postgres    false    203          "           0    0    bookings_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.bookings_id_seq', 1, false);
          public          postgres    false    206            #           0    0    properties_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.properties_id_seq', 1, true);
          public          postgres    false    204            $           0    0    users_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.users_id_seq', 1, true);
          public          postgres    false    202            �
           2606    16483    bookings bookings_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.bookings DROP CONSTRAINT bookings_pkey;
       public            postgres    false    207            �
           2606    16476    properties properties_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.properties
    ADD CONSTRAINT properties_pkey PRIMARY KEY (id);
 D   ALTER TABLE ONLY public.properties DROP CONSTRAINT properties_pkey;
       public            postgres    false    205            �
           2606    16435    users users_pkey 
   CONSTRAINT     N   ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
 :   ALTER TABLE ONLY public.users DROP CONSTRAINT users_pkey;
       public            postgres    false    203            �
           1259    16495    idx_bookings_property_dates    INDEX     m   CREATE INDEX idx_bookings_property_dates ON public.bookings USING btree (property_id, start_date, end_date);
 /   DROP INDEX public.idx_bookings_property_dates;
       public            postgres    false    207    207    207            �
           1259    16494    idx_bookings_property_id    INDEX     T   CREATE INDEX idx_bookings_property_id ON public.bookings USING btree (property_id);
 ,   DROP INDEX public.idx_bookings_property_id;
       public            postgres    false    207            �
           1259    16496    idx_bookings_user_property    INDEX     _   CREATE INDEX idx_bookings_user_property ON public.bookings USING btree (user_id, property_id);
 .   DROP INDEX public.idx_bookings_user_property;
       public            postgres    false    207    207            �
           2606    16484    bookings property_id    FK CONSTRAINT     �   ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT property_id FOREIGN KEY (property_id) REFERENCES public.properties(id) NOT VALID;
 >   ALTER TABLE ONLY public.bookings DROP CONSTRAINT property_id;
       public          postgres    false    207    205    2704            �
           2606    16489    bookings user_id    FK CONSTRAINT     y   ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(id) NOT VALID;
 :   ALTER TABLE ONLY public.bookings DROP CONSTRAINT user_id;
       public          postgres    false    2702    203    207                  x������ � �            x�3�,)���L�(�4442����� C�)            x�3��M,J����� ��     