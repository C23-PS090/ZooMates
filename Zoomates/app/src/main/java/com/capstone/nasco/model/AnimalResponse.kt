package com.capstone.nasco.model

import com.google.gson.annotations.SerializedName

data class AnimalResponse(

	@field:SerializedName("animalResponse")
	val animalResponse: List<AnimalResponseItem>
)

data class AnimalResponseItem(

	@field:SerializedName("kemampuan")
	val kemampuan: String? = null,

	@field:SerializedName("habitat")
	val habitat: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("pernapasan")
	val pernapasan: String? = null,

	@field:SerializedName("tubuh")
	val tubuh: String? = null,

	@field:SerializedName("makanan")
	val makanan: String? = null,

	@field:SerializedName("nama_hewan")
	val namaHewan: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("reproduksi")
	val reproduksi: String? = null,

	@field:SerializedName("jenis")
	val jenis: String? = null,

	@field:SerializedName("pergerakan")
	val pergerakan: String? = null,

	@field:SerializedName("diet")
	val diet: String? = null,

	@field:SerializedName("id_animal")
	val idAnimal: Int? = null
)
